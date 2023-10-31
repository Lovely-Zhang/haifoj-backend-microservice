# 使用一个基础镜像
FROM openjdk:8-jdk-alpine

# 设置工作目录
WORKDIR /app

# 复制haifoj-model-0.0.1-SNAPSHOT.jar到镜像中
# COPY haifoj-question-service-0.0.1-SNAPSHOT.jar /app/haifoj-question-service-0.0.1-SNAPSHOT.jar

# 将 jar 包添加到工作目录，比如 haifoj-model-0.0.1-SNAPSHOT.jar，  . 添加到当前目录
ADD target/haifoj-question-service-0.0.1-SNAPSHOT.jar .

# 暴露端口
EXPOSE 8103

# 定义默认的运行命令
CMD ["java", "-jar", "/app/haifoj-question-service-0.0.1-SNAPSHOT.jar", "--spring.profiles.active=prod"]