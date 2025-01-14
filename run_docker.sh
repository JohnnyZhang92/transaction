#!/bin/bash

# 容器名称
CONTAINER_NAME="transaction-8080"
IMAGE_NAME="transaction"
PORT_MAPPING="8080:8080"
MEMORY="2g"
CPU_NUM="1"
# 打印帮助信息
print_help() {
    echo "Usage: $0 [start|stop|restart]"
    echo "start: 启动Docker容器"
    echo "stop:  停止Docker容器"
    echo "restart: 重启Docker容器"
}

# 启动容器
start_container() {
    container_count=$(docker ps -a -q --filter name=$CONTAINER_NAME | wc -l)
    if [ "$container_count" -eq 1 ]; then
        docker start $CONTAINER_NAME
        if [ $? -eq 0 ]; then
            echo "容器 $CONTAINER_NAME 已启动。"
        else
            echo "启动容器 $CONTAINER_NAME 失败。"
        fi
    else
        docker run -d -p $PORT_MAPPING --name $CONTAINER_NAME $IMAGE_NAME --memory=$MEMORY --cpus=$CPU_NUM
        if [ $? -eq 0 ]; then
            echo "容器 $CONTAINER_NAME 已创建并启动。"
        else
            echo "创建并启动容器 $CONTAINER_NAME 失败。"
        fi
    fi
}

# 停止容器
stop_container() {
    if docker ps -q --filter name=$CONTAINER_NAME > /dev/null; then
        docker stop $CONTAINER_NAME
        if [ $? -eq 0 ]; then
            echo "容器 $CONTAINER_NAME 已停止。"
        else
            echo "停止容器 $CONTAINER_NAME 失败。"
        fi
    else
        echo "容器 $CONTAINER_NAME 未运行或不存在。"
    fi
}
# 根据传入参数执行相应操作
case "$1" in
    start)
        start_container
        ;;
    stop)
        stop_container
        ;;
    restart)
        stop_container
        start_container
        ;;
    *)
        print_help
        ;;
esac