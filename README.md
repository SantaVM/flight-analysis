# Тестовое задание

*****************************

## Описание задания

Напишите программу на языке программирования java, которая прочитает файл tickets.json и рассчитает:

- Минимальное время полета между городами Владивосток и Тель-Авив для каждого авиаперевозчика
- Разницу между средней ценой  и медианой для полета между городами  Владивосток и Тель-Авив

Программа должна вызываться из командной строки Linux, результаты должны быть представлены в текстовом виде.

*****************************

## Запуск приложения

> Чтобы была возможность запустить проект из командной строки, его надо скомпилировать в исполняемый jar архив с помощью команды

```bash
mvn clean package
```

> Переходим в папку ./target и копируем в неё файл с расписанием полётов: tickets.json
> После этого запускаем на выполнение с помощью команды

```bash
java -jar FlightAnalysis-1.0-SNAPSHOT.jar
```
