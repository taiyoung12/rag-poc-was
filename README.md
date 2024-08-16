# rag-poc

이 프로젝트는 Spring Boot로 구현된 RAG 시스템의 서버 애플리케이션입니다. 해당 애플리케이션은 RabbitMQ 메시지 큐를 사용하여 키워드와 프롬프트를 처리하고, 외부 API에 요청을 보내 LLM (Large Language Model) 응답을 처리하는 기능을 제공합니다.

## 프로젝트 스펙

- **Framework**: Spring Boot 3.3.2
- **Language**: Kotlin
- **Test Library**: JUnit
- **MessageQueue**: RabbitMQ

## 프로젝트 구조

- **Controller**: 웹 요청을 처리하고 메시지를 큐에 넣는 역할을 합니다.
- **Service**: RabbitMQ에서 메시지를 구독하고, 외부 API를 호출하여 결과를 처리합니다.
- **Listener**: 메시지 큐에서 메시지를 수신하고 처리합니다.
- **External API Client**: 외부 LLM API와 통신하는 클라이언트입니다.
- **Test**: 통합 테스트가 포함되어 있으며, 애플리케이션의 전반적인 흐름이 제대로 작동하는지 검증합니다.

## 주요 기능

- **RAG 요청 처리**: 키워드와 프롬프트를 입력받아 RabbitMQ 큐에 메시지를 전송합니다.
- **LLM 응답 처리**: RabbitMQ에서 메시지를 수신하고 외부 API로 해당 메시지를 전송하여 LLM의 응답을 받아 처리합니다.

## RabbitMQ Docker 실행 방법

RabbitMQ는 Docker를 사용해 쉽게 실행할 수 있습니다. 아래의 명령어를 사용하여 RabbitMQ 컨테이너를 실행하세요.

1. **Docker 설치**
   Docker가 설치되어 있지 않다면, [Docker 공식 사이트](https://www.docker.com/get-started)에서 설치하세요.

2. **RabbitMQ Docker 이미지 다운로드 및 실행**

   ```bash
   docker run -d --name rabbitmq -p 5672:5672 -p 15672:15672 rabbitmq:3-management