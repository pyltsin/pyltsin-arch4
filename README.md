# Запуск
Перейти в папку k8s и выполнить

установить зависимости
helm dependency update
и
helm install products products

- установится сервис
- установится Memcached
- установится PostgreSQL

Сразу в базe появятся 1000 сгенерированных случайным образом записей

# Краткое описание

При запросе проверяется были ли такие же запросы, если нет - то кладутся в кэш.
Если были - используются данные из кэша.
Кэш инвалидируется через 1 минуту

# Тестирование

Из основной папки выполнить

newman run cache.postman_collection.json

Выполнятся 2 запроса 
- 1 запрос - положит результаты в кэш
- 2 запрос - будет уже использовать кэш

По умолчанию запросы делаются на домен: arch.homework

Чтобы проверить что сработал именно кэш посмотреть логи pod products

  kubectl logs -f products-6c95c59d94-g5lfp 
  
6c95c59d94-g5lfp -- будут отличатся

Если кэш НЕ работает в конце будут два сообщения типа:

INFO 1 --- [nio-8000-exec-8] com.otus.arch4.ProductService            : miss
INFO 1 --- [nio-8000-exec-8] com.otus.arch4.ProductService            : miss

Если кэш сработал то будет:

INFO 1 --- [nio-8000-exec-8] com.otus.arch4.ProductService            : miss
INFO 1 --- [nio-8000-exec-8] com.google.code.ssm.spring.SSMCache      : Put ...
INFO 1 --- [nio-8000-exec-9] com.google.code.ssm.spring.SSMCache      : Cache hit ...


# Отключение

В папке k8s/products заменить
ENABLE_CACHE: "1"
на
ENABLE_CACHE: "0" и передеплоить