## 프로젝트 명

스마트 허리보호대<br><br>

## 프로젝트 소개
의료 기술의 발달과 생활 수준의 향상으로 평균 수명이 연장 + 출산율저하로 독거노인의 비율이 증가하고 이에 따른 고독사를 방지하기위해 고안하였으며, 
생체 정보를 감지하는 센서를 구비한 보호대(IOT)와 연동하여 착용자의신체 상태를 실시간으로 모니터링할수있는 어플리케이션 개발 프로젝트, 사용자뿐만아니라
보호자가 사용자의 생체정보를 실시간으로 모니터링가능하다.<br><br>

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
* Android (
* Firebase Auth
* Firebase RealtimeDB
* Firebase Cloud Messaging
* Glide
<br><br>

## 시스템 아키텍처
![컴공설 nerd설계](https://user-images.githubusercontent.com/48284360/98101591-c9f3d680-1ed5-11eb-90ec-a6b98c9763d0.png)<br><br>



## 스크린샷
#### 메인 화면
<div>
<img width="200" src="https://user-images.githubusercontent.com/48284360/98079157-b5ecac80-1eb6-11eb-83b7-3e9cfa7fd642.jpg"> 
<img width="200" src="https://user-images.githubusercontent.com/48284360/98079151-b4bb7f80-1eb6-11eb-8a18-bb7c89d45b1e.jpg"> <br><br>
</div>

#### 사용자 페이지
<div>
<img width="200" src="https://user-images.githubusercontent.com/48284360/98079161-b71dd980-1eb6-11eb-9ebc-867bddcfe1ae.jpg">
<img width="200" src="https://user-images.githubusercontent.com/48284360/98079148-b422e900-1eb6-11eb-8b35-c1e597499a08.jpg"> 
<img width="200" src="https://user-images.githubusercontent.com/48284360/98079153-b5541600-1eb6-11eb-880c-b4095247be11.jpg">
<br><br>
</div>

#### 보호자 페이지
<div>
<img width="200"  src="https://user-images.githubusercontent.com/48284360/98079164-b7b67000-1eb6-11eb-9f8a-8bec319dca89.jpg">
<img width="200" src="https://user-images.githubusercontent.com/48284360/98079140-b2592580-1eb6-11eb-9a89-3d40435ac11e.jpg">
  <br><br>
</div>

#### 공통 페이지
<div>
<img width="200" src="https://user-images.githubusercontent.com/48284360/98079156-b5541600-1eb6-11eb-8f3a-714138e056cb.jpg">
<img width="200" src="https://user-images.githubusercontent.com/48284360/98079162-b71dd980-1eb6-11eb-96dd-8de9c92eaf7f.jpg">
<img width="200" src="https://user-images.githubusercontent.com/48284360/98079159-b6854300-1eb6-11eb-816f-51bbc12c42d6.jpg">

</div>

## 개선사항
모니터링에 그치지않고, 위험상황분석을 통해 보호자에게 위험시 사용자에 대한 위험정도와, 사용자의 위치를 알림으로 줄수있는 서비스를 추가 및 개선이 필요하다.


