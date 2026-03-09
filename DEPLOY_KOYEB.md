# Koyeb Deployment Guide (Spring Boot + Kafka + SMTP)

## 1) Push code
- Push this project to your Git provider (`GitHub`/`GitLab`/`Bitbucket`).

## 2) Create Koyeb Service
- In Koyeb, create a new **Web Service** from your repository.
- Runtime:
  - Use the repository Dockerfile.
  - Exposed port: `8080` (or keep `PORT` env var set by Koyeb).
- Health check:
  - Path: `/actuator/health`

## 3) Set Required Environment Variables
- Use values from [`.env.koyeb.example`](/home/sagar/Documents/spring_boot_farm_connect/krishisetu/.env.koyeb.example).
- Minimum required:
  - `SPRING_PROFILES_ACTIVE=prod`
  - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
  - `JWT_SECRET`
  - `APP_BACKEND_BASE_URL`
  - `APP_FRONTEND_RESET_PASSWORD_URL`
  - `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`
  - `KAFKA_BOOTSTRAP_SERVERS`
  - `KAFKA_SECURITY_PROTOCOL`
  - `KAFKA_SASL_MECHANISM`
  - `KAFKA_SASL_JAAS_CONFIG`

## 4) Kafka Credential Mapping (Managed Kafka)
- Most managed providers give:
  - bootstrap server(s)
  - username / password (or API key / secret)
  - SASL mechanism (`PLAIN` typically)
  - security protocol (`SASL_SSL`)
- Set:
  - `KAFKA_BOOTSTRAP_SERVERS=<host1:9092,host2:9092>`
  - `KAFKA_SECURITY_PROTOCOL=SASL_SSL`
  - `KAFKA_SASL_MECHANISM=PLAIN`
  - `KAFKA_SASL_JAAS_CONFIG=org.apache.kafka.common.security.plain.PlainLoginModule required username="<USERNAME>" password="<PASSWORD>";`

## 5) SMTP Notes
- For Gmail SMTP, use App Password (not account password):
  - `MAIL_HOST=smtp.gmail.com`
  - `MAIL_PORT=587`
  - `MAIL_SMTP_AUTH=true`
  - `MAIL_SMTP_STARTTLS_ENABLE=true`

## 6) Production Config Files
- Properties profile: [`application-prod.properties`](/home/sagar/Documents/spring_boot_farm_connect/krishisetu/src/main/resources/application-prod.properties)
- YAML profile: [`application-prod.yml`](/home/sagar/Documents/spring_boot_farm_connect/krishisetu/src/main/resources/application-prod.yml)

Use one style consistently for future edits. Both currently contain equivalent production settings.

## 7) GitHub Actions Auto-Deploy (already added)
- Workflow file: [deploy-koyeb.yml](/home/sagar/Documents/spring_boot_farm_connect/krishisetu/.github/workflows/deploy-koyeb.yml)
- This deploys automatically on push to `main` and can also be triggered manually.

### You need to do this once in GitHub
- Go to `Repo -> Settings -> Secrets and variables -> Actions`.
- Add `Secrets`:
  - `KOYEB_API_TOKEN`
  - `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`
  - `JWT_SECRET`
  - `APP_BACKEND_BASE_URL`, `APP_FRONTEND_RESET_PASSWORD_URL`
  - `MAIL_HOST`, `MAIL_PORT`, `MAIL_USERNAME`, `MAIL_PASSWORD`
  - `KAFKA_BOOTSTRAP_SERVERS`
  - `KAFKA_SECURITY_PROTOCOL`
  - `KAFKA_SASL_MECHANISM`
  - `KAFKA_SASL_JAAS_CONFIG`
- Optional `Repository variables`:
  - `KOYEB_APP_NAME` (default `krishisetu-app`)
  - `KOYEB_SERVICE_NAME` (default `krishisetu-api`)
  - `KOYEB_REGION` (default `fra`)
  - `KOYEB_INSTANCE_TYPE` (default `nano`)

### First deployment
- Push to `main` branch, or go to `Actions -> Deploy to Koyeb -> Run workflow`.
- Watch logs in GitHub Actions.
- After deployment, verify:
  - `GET https://<your-service-domain>/actuator/health`
  - Register user -> verification email received
  - Forgot password -> reset email received
