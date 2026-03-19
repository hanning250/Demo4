#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <getopt.h>

#define BUF_SIZE 4096
#define DEFAULT_PORT 8888
#define DEFAULT_INTERVAL 2

void usage(const char *prog) {
    fprintf(stderr, "用法: %s [选项] <服务器IP>\n", prog);
    fprintf(stderr, "选项:\n");
    fprintf(stderr, "  -p <端口>    指定服务端端口（默认 %d）\n", DEFAULT_PORT);
    fprintf(stderr, "  -r <秒>      实时刷新模式，每隔指定秒数更新一次数据\n");
    fprintf(stderr, "  -h           显示帮助信息\n");
}

int main(int argc, char *argv[]) {
    char *server_ip = NULL;
    int port = DEFAULT_PORT;
    int interval = DEFAULT_INTERVAL;
    int realtime = 0;
    int opt;

    while ((opt = getopt(argc, argv, "p:r:h")) != -1) {
        switch (opt) {
            case 'p':
                port = atoi(optarg);
                break;
            case 'r':
                realtime = 1;
                interval = atoi(optarg);
                break;
            case 'h':
                usage(argv[0]);
                return 0;
            default:
                usage(argv[0]);
                return 1;
        }
    }

    if (optind >= argc) {
        fprintf(stderr, "错误: 必须指定服务器IP\n");
        usage(argv[0]);
        return 1;
    }
    server_ip = argv[optind];

    // 创建 socket
    int sockfd = socket(AF_INET, SOCK_STREAM, 0);
    if (sockfd < 0) {
        perror("创建 socket 失败");
        return 1;
    }

    struct sockaddr_in server_addr;
    memset(&server_addr, 0, sizeof(server_addr));
    server_addr.sin_family = AF_INET;
    server_addr.sin_port = htons(port);
    if (inet_pton(AF_INET, server_ip, &server_addr.sin_addr) <= 0) {
        perror("无效的 IP 地址");
        close(sockfd);
        return 1;
    }

    // 连接服务端
    if (connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
        perror("连接服务器失败");
        close(sockfd);
        return 1;
    }

    printf("成功连接到 %s:%d\n", server_ip, port);

    do {
        char buf[BUF_SIZE] = {0};
        ssize_t n = recv(sockfd, buf, sizeof(buf)-1, 0);
        if (n <= 0) {
            perror("接收数据失败或连接断开");
            break;
        }
        printf("\n================== 监控数据 ==================\n");
        printf("%s", buf);
        printf("==============================================\n");

        if (!realtime) break; // 非实时模式，获取一次就退出

        sleep(interval);

        // 重新连接（因为服务端是短连接）
        close(sockfd);
        sockfd = socket(AF_INET, SOCK_STREAM, 0);
        if (sockfd < 0 || connect(sockfd, (struct sockaddr*)&server_addr, sizeof(server_addr)) < 0) {
            perror("实时模式下重连失败");
            break;
        }
    } while (realtime);

    close(sockfd);
    return 0;
}
