# 使用一个已经安装好 Java 17 的官方基础镜像
FROM openjdk:17-jdk-slim

# 设置一个工作目录
WORKDIR /app

# 把我们编译好的 .jar 文件复制到这个盒子里，并重命名为 app.jar
# 注意：这里的 library-1.0.0.jar 必须和你的 pom.xml 里的 artifactId 和 version 对应
COPY target/library-1.0.0.jar app.jar

# 当盒子启动时，就运行这个命令
ENTRYPOINT ["java", "-jar", "app.jar"]