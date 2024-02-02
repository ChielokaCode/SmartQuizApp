FROM openjdk:17
WORKDIR /app
COPY SmartQuizApp-0.0.1-SNAPSHOT.jar /app/SmartQuiz-app.jar
EXPOSE 8080
#RUN javac Main.java
CMD ["java", "-jar", "SmartQuiz-app.jar"]