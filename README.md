# 📖 OOP Smart Library Reservation System

> 객체지향프로그래밍 프로젝트 13조  
> 도서관 좌석 추천 및 예약 시스템
<br>

## 📌 프로젝트 소개

사용자에게 맞는 도서관 좌석과 스터디룸을 추천하고, 예약 및 후기/상태 등록 기능을 제공하는 Java Swing 기반 프로그램입니다.

<br>

## ✨ 핵심 기능

- 테스트 기반 공간 추천: 개인별 성향을 분석하여 상위 3개 공간 추천
- 공간 상세 조회: 선택한 공간의 상세 정보, 최근 상태, 후기 목록 출력
- 좌석 및 스터디룸 예약: `synchronized` 기반 동시 예약 충돌 방지 및 별도 스레드를 활용한 예약 기능
- 예약 현황 관리: 현재 사용 중인 예약과 지난 예약을 구분하여 조회
- 통합 피드백 등록: 공간 이용 후 후기(별점/한줄평) 및 현재 상태 기록(소음/혼잡도 등) 익명 등록
<br>

## 👥 Team & Role

| 이름 | 담당 역할 | 핵심 구현 컴포넌트 |
|---|---|---|
| 김사랑 | 홈 네비 및 공간 추천 | 화면 전환 설계, 성향 테스트 알고리즘, 최근 상태 평균 연산, 조회 관련 GUI |
| 윤소희 | 사용자 식별 및 예약 시스템 | 사용자 인증, 멀티스레드 동기화, 백그라운드 타이머 스레드, 예약 관련 GUI |
| 곽해림 | 공간/후기/상태 데이터 관리 | 모델 상속·다형성 구조 설계, 파일 I/O 기반 Repository 영속성 제어, 등록 GUI |
<br>

## 🧩 Architecture (MVC Pattern)

```text
Model
├── Domain
│   ├── User
│   ├── Space
│   │   ├── Seat
│   │   └── StudyRoom
│   ├── Reservation
│   ├── Review
│   ├── Status
│   ├── PreferenceTest
│   └── PreferenceResult
├── Service / Logic
│   └── Recommender
└── Repository
    ├── BaseRepository
    ├── SpaceRepository
    ├── ReservationRepository
    ├── ReviewRepository
    └── StatusRepository

View
├── BaseView
├── HomeView
├── TestView
├── RecommendView
├── SpaceDetailView
├── ReservationView
├── MyReservationView
└── SpaceFormView

Controller
├── NavigationController
├── TestController
├── RecommendController
├── SpaceController
├── ReservationController
├── MyReservationController
└── SpaceFormController
