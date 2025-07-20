**Synthetic Human Core Starter** — это ядро для управления андроидами (синтетиками), которое:
- Принимает команды от операторов
- Обрабатывает их с разными приоритетами
- Ведёт аудит всех действий
- Мониторит загрузку системы
- Обрабатывает ошибки

---
Для быстрого перехода к инструкции по запуску:
[нажмите сюда](#docker-instructions)

### **Технологический стек**
```
Spring Boot 3
├─ Spring Web (REST)
├─ Spring Kafka
├─ Spring AOP (аспекты)
├─ Micrometer (метрики)
└─ Lombok
```

### **Диаграмма компонентов**
![Диаграмма](assets/workshop-4.png)
---

### **Ключевые компоненты**

#### **A. Command Controller (REST API)**
- **Что делает**: Принимает команды в формате:
  ```json
  {
    "description": "Проверить двигатели",
    "priority": "CRITICAL",
    "author": "Рипли",
    "time": "2023-01-01T00:00:00Z"
  }
  ```
- **Пример запроса**:
  ```bash
  curl -X POST http://localhost:8080/api/commands -H "Content-Type: application/json" -d '{"description":"Check engines","priority":"CRITICAL","author":"Ripley","time":"2023-01-01T00:00:00Z"}'
  ```

#### **B. Command Processor**
- **Логика работы**:
  ```
  if (priority == CRITICAL) → выполнить немедленно
  else → добавить в очередь (макс. 10 команд)
  ```
- **Потоки**:
    - Главный поток: принимает команды
    - Фоновый поток: обрабатывает очередь

#### **C. Audit Aspect**
- **Как работает**:
  ```java
  @WeylandWatchingYou(mode = AuditMode.KAFKA)
  public String criticalOperation() { ... }
  ```
  Логирует:
    - Имя метода
    - Параметры
    - Результат
    - Время выполнения

#### **D. Kafka Integration**
- Отправляет аудит-логи в топики:
    - `android-audit` — успешные операции
    - `android-audit-errors` — ошибки

#### **E. Мониторинг**
- Метрики доступны на:
  ```
  http://localhost:8080/actuator/metrics
  http://localhost:8080/actuator/prometheus
  ```
  Собирает:
    - Размер очереди команд
    - Количество выполненных задач

---

### **Пример Workflow**

1. **Оператор** отправляет команду через REST API
2. **Система**:
    - Валидирует команду
    - CRITICAL → выполняет сразу
    - COMMON → ставит в очередь
3. **Аудит**:
    - Записывает действие в Kafka/консоль
4. **Мониторинг**:
    - Обновляет метрики (очередь, performance)

<a id="docker-instructions"></a>

### 🐳Сборка и запуск
   Соберите проект:

```bash
./mvnw clean package
```
Запустите все сервисы:

```bash
docker-compose up -d --build
```
Проверьте сервисы:

Приложение: http://localhost:8080/api/commands

Prometheus: http://localhost:9090

Grafana: http://localhost:3000 (логин: admin/admin)