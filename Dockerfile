# ---- 第一阶段：编译项目 ----
# 使用一个带有 Maven 和 Java 17 的镜像作为“编译房”
FROM maven:3.8.5-openjdk-17 AS build

# 设置工作目录
WORKDIR /app

# 先只复制 pom.xml，这样可以利用 Docker 的缓存机制
COPY pom.xml .
# 下载所有依赖
RUN mvn dependency:go-offline

# 复制我们所有的 Java 源代码
COPY src ./src

# 运行 Maven 命令来编译项目，生成 .jar 文件
RUN mvn package -DskipTests


# ---- 第二阶段：打包运行 ----
# 使用一个非常小的、只包含 Java 运行环境的镜像作为“运行房”
FROM openjdk:17-jdk-slim

# 设置工作目录
WORKDIR /app

# 从第一阶段的“编译房”里，把我们编译好的 .jar 文件复制到这个最终的“运行房”里
COPY --from=build /app/target/library-1.0.0.jar app.jar

# 当最终的盒子启动时，就运行这个命令
ENTRYPOINT ["java", "-jar", "app.jar"]