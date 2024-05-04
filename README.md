## Утилита для анализа отказов в access-логе

### На вход принимает параметры:

**-u** минимально допустимый уровень доступности

**-t** приемлемое время ответа в мс


### Читает лог в формате:
```
192.168.32.181 - - [14/06/2017:16:47:02 +1000] "PUT /rest/v1.4/documents?zone=default&_rid=6076537c HTTP/1.1" 200 2 44.510983 "-" "@list-item-updater" prio:0
```

### На выходе отдает статистику отказов:
```
2017-06-14T06:47:02 2017-06-14T06:47:04 25,0
2017-06-14T06:47:12 2017-06-14T06:47:16 40,0
```
где даты - диапазон времени, когда уровень доступности был ниже допустимого, число - уровень доступности

### Пример запуска:
```
java -Xmx512M -jar AccessLogAnalyzer.jar -u 90 -t 50 < log.log
```