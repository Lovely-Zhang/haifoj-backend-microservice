# 使用一个基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制haifoj-model-0.0.1-SNAPSHOT.jar到镜像中
# COPY haifoj-user-service-0.0.1-SNAPSHOT.jar /app/haifoj-user-service-0.0.1-SNAPSHOT.jar

# 将 jar 包添加到工作目录，比如 haifoj-model-0.0.1-SNAPSHOT.jar，  . 添加到当前目录
ADD target/haifoj-user-service-0.0.1-SNAPSHOT.jar .

# 定义默认的运行命令
CMD ["java", "-jar", "/app/haifoj-user-service-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]