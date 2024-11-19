# Результаты работы бенчмарка

```
Benchmark                               (scenario)   Mode  Cnt   Score   Error   Units
KafkaBenchmark.testProducerConsumer            1:1  thrpt    5   1,163 ± 0,192  ops/ms
KafkaBenchmark.testProducerConsumer            3:1  thrpt    5   1,255 ± 0,099  ops/ms
KafkaBenchmark.testProducerConsumer            1:3  thrpt    5   0,985 ± 0,016  ops/ms
KafkaBenchmark.testProducerConsumer            3:3  thrpt    5   1,230 ± 0,028  ops/ms
KafkaBenchmark.testProducerConsumer          10:10  thrpt    5   0,656 ± 0,028  ops/ms
RabbitMQBenchmark.testProducerConsumer         1:1  thrpt    5  47,626 ± 1,504  ops/ms
RabbitMQBenchmark.testProducerConsumer         3:1  thrpt    5  16,657 ± 3,080  ops/ms
RabbitMQBenchmark.testProducerConsumer         1:3  thrpt    5  51,815 ± 2,406  ops/ms
RabbitMQBenchmark.testProducerConsumer         3:3  thrpt    5  16,760 ± 1,203  ops/ms
RabbitMQBenchmark.testProducerConsumer       10:10  thrpt    5   4,584 ± 0,191  ops/ms
```
## Краткий вывод о конфигурациях продюсеров и консюмеров
- **1 продюсер и 1 консюмер:**
  - Лучшая система: Kafka.
  - Причина: Высокая производительность и минимальная задержка. Подходит для простых сценариев передачи сообщений.
- **3 продюсера и 1 консюмер** 
  - Лучшая система: Kafka.
  - Причина: Наивысшая производительность при высокой частоте генерации сообщений. RabbitMQ испытывает затруднения с масштабируемостью продюсеров.
- **1 продюсер и 3 консюмера** 
  - Лучшая система: RabbitMQ. 
  - Причина: Максимальная производительность за счет pre-fetching и гибкости маршрутизации.
- **3 продюсера и 3 консюмера** 
  - Лучшая система: RabbitMQ. 
  - Причина: Хорошо справляется с балансировкой нагрузки между участниками. Kafka работает стабильнее, но медленнее.
- **10 продюсеров и 10 консюмеров** 
  - Лучшая система: RabbitMQ. 
  - Причина: Лучше справляется с нагрузкой, но оба брокера испытывают трудности.