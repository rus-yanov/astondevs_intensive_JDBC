run:
	./build/install/app/bin/app

clean:
	./gradlew clean

build:
	./gradlew clean war

install:
	./gradlew clean installDist

test:
	./gradlew test

report:
	./gradlew jacocoTestReport

lint:
	./gradlew checkstyleMain checkstyleTest

start-dist:
	APP_ENV=production ./build/install/app/bin/app

start:
	APP_ENV=development ./gradlew run

.PHONY: build
