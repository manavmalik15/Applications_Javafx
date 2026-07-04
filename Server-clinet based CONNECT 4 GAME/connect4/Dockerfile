FROM eclipse-temurin:23-jdk

WORKDIR /app

COPY src ./src
COPY user_data.csv ./

RUN javac -d out $(find src/main/java -name "*.java")

EXPOSE 5555

CMD ["java", "-cp", "out", "org.connect4.Server"]
