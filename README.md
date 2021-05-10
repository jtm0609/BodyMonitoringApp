## 프로젝트 명

스마트 허리보호대<br><br>

## 프로젝트 소개
평균 수명이 연장과 출산율저하 문제로 홀몸노인의 비율이 증가함에따라 고독사를 방지 하기 위해 고안하였다.
생체 정보를 감지하는 센서를 구비한 보호대(IOT)와 연동하여 착용자의신체 상태를 실시간으로 모니터링 할 수 있는 앱 프로젝트 이다.
사용자는 자신의 생채 상태를 모니터링 할 수있으며, 보호자는 사용자를 친구로 등록하여 사용자의 생체정보를 실시간으로 모니터링 할 수 있다..<br><br>

### 프로젝트 기간
2020.02.01 ~ 2020 04.01<br><br>

### 역할 배정
앱개발 1명, 회로개발1명<br><br>

### 본인 역할

#### 생체 정보 모니터링을 위한 모바일앱 개발(Java)
* 파이어베이스 Auth 이용(전화번호)
* 파이어베이스 RealTimeDB를 통해 유저(사용자, 보호자) 관리
* 아두이노와 앱과의 연동 구현(블루투스 통신)
* 센서를 통해 실시간으로 생체정보(심박수, 체온, 낙상) 모니터링 가능
* Service와 BroadCast Receiver를 통해 항상 센서가 동작하게 구현 및 블루투스 연결, 끊김 체크
* 친구목록 기능 구현
* 친구요청 기능 구현 + FCM을 통한 친구요청 알림서비스 구현
* 추천 친구 기능 구현(컨텐트 프로바이더 사용)
* 친구가된 보호자는 사용자의 생체정보를 실시간 모니터링 가능<br><br>




## 기술스택
* Android (JAVA)
* BlueTooth API
* Firebase Auth
* Firebase RealtimeDB
* Firebase Cloud Messaging
* Glide
<br><br>

## 시스템 아키텍처
![컴공설 nerd설계](https://user-images.githubusercontent.com/48284360/98101591-c9f3d680-1ed5-11eb-90ec-a6b98c9763d0.png)<br><br>


## SW 설계(시퀀스 다이어그램)
<div>
  <img  src="https://user-images.githubusercontent.com/48284360/117598450-cee65b00-b182-11eb-8226-8e89efca5c48.png"> <br><br><br><br><br>
<img src="https://user-images.githubusercontent.com/48284360/117598458-d1e14b80-b182-11eb-9fd6-523c2d9423e8.png"> <br><br><br><br><br>
<img  src="https://user-images.githubusercontent.com/48284360/117598461-d279e200-b182-11eb-85b6-fbb8f5d4fa4b.png"> <br><br><br><br><br>
<img  src="https://user-images.githubusercontent.com/48284360/117598464-d3ab0f00-b182-11eb-9dad-a8a1d8695714.png"> <br><br><br><br><br>
<img  src="https://user-images.githubusercontent.com/48284360/117598469-d443a580-b182-11eb-8b53-6892554430f3.png"> <br><br><br><br><br>
<img  src="https://user-images.githubusercontent.com/48284360/117598472-d4dc3c00-b182-11eb-967c-87f4d7e1d9fe.png"> <br><br><br>
</div>

## Database
![DB설계](https://user-images.githubusercontent.com/48284360/117599003-e540e680-b183-11eb-939d-5109f650910e.png)
<br><br><br>

## 스크린샷
<div>
  <img  src="https://user-images.githubusercontent.com/48284360/111037574-05944380-8468-11eb-9702-06b5067b1efb.png">
<img src="https://user-images.githubusercontent.com/48284360/111037568-0331e980-8468-11eb-9493-f58350438489.png"> 
<img  src="https://user-images.githubusercontent.com/48284360/111037695-a1be4a80-8468-11eb-95b6-9ea1fc9cbc56.png">
<img  src="https://user-images.githubusercontent.com/48284360/111037572-05944380-8468-11eb-8912-6f0bc5dfaee8.png"> <br><br>
</div>


## 개선사항
모니터링에 그치지않고, 위험 상황 분석을 통해 보호자에게 사용자에 대한 위험정도와, 사용자의 위치를 알림으로 줄수있는 서비스를 추가 및 개선이 필요하다.


