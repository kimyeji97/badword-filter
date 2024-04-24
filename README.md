# badword-filter


비속어 필터링 라이브러리

## 개발 환경

- gradle 8.0

## 제공 기능

작성전.


## 사용 방법

텍스트 파일 저장 위치 : resources/templates/*.txt

### properties


| key               | type         | value                   |
|-------------------|--------------|-------------------------|
| badword.masking   | string       | 마스킹 문자    (default : *) |
| badword.paths.txt | list<string> | 비속어 텍스트 파일 경로           |
```yaml
badword:
  masking: '#'
  paths:
    txt: >
      classpath*:templates/bad-words.txt
      , classpath*:bad-words/*.txt
```