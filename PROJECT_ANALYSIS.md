# 우정교회 홈페이지 프로젝트 분석

> Vue3 + Nuxt3 마이그레이션을 위한 현행 시스템 분석 문서

---

## 1. 프로젝트 개요

| 항목 | 내용 |
|------|------|
| 프로젝트명 | 우정교회 홈페이지 |
| 패키지 | org.woojeong |
| 프레임워크 | Spring Boot 2.4.3 |
| 빌드 도구 | Gradle |
| 서버 포트 | 5000 |
| 배포 환경 | AWS Elastic Beanstalk (wjhome-env.eba-x82zpk3p.ap-northeast-2.elasticbeanstalk.com) |
| 도메인 | woojeong.or.kr |
| HTTPS | 미적용 |
| 데이터베이스 | AWS RDS MySQL (ap-northeast-2) |
| 파일 스토리지 | AWS S3 (kor-wj-bucket) |

---

## 2. 프로젝트 구조

```
src/main/java/org/woojeong/
├── WjApplication.java
├── api/v1/
│   ├── account/           # 회원가입/인증
│   │   ├── AccountRestController.java
│   │   ├── AccountService.java
│   │   ├── AccountDao.java
│   │   └── dto/RegisterDTO.java
│   ├── board/             # 설교/커뮤니티 게시판
│   │   ├── BoardRestController.java
│   │   ├── BoardService.java
│   │   ├── BoardDao.java
│   │   ├── dto/
│   │   │   ├── WorshipRegisterDTO.java
│   │   │   ├── WorshipBoardListDTO.java
│   │   │   ├── CommunityBoardListDTO.java
│   │   │   └── CommunityRegisterDTO.java
│   │   └── vo/
│   │       ├── WorshipBoardVo.java
│   │       ├── CommunityBoardVo.java
│   │       ├── WorshipBoardListPaging.java
│   │       └── CommunityBoardListPaging.java
│   ├── hidden/            # 기도 요청 (비공개)
│   │   ├── PrayerRestController.java
│   │   ├── PrayerService.java
│   │   ├── PrayerDao.java
│   │   ├── dto/InsertPrayerContentDTO.java
│   │   └── vo/PrayerListVo.java
│   └── common/            # 공통 데이터
│       ├── CommonRestController.java
│       ├── CommonService.java
│       └── CommonDao.java
├── config/
│   ├── database/DatabaseConfig.java    # MyBatis 설정
│   └── security/
│       ├── auth/
│       │   ├── SecurityConfiguration.java
│       │   ├── AuthManager.java
│       │   ├── ajax/                   # AJAX 로그인 처리
│       │   └── jwt/                    # JWT 토큰 처리
│       ├── user/
│       │   ├── WjUserService.java
│       │   ├── WjUserInfo.java
│       │   └── WjUserVo.java
│       └── etc/AjaxAccessDeniedHandler.java
├── web/WebController.java              # 페이지 라우팅 (Thymeleaf)
└── util/
    ├── S3Uploader.java                 # AWS S3 업로드
    ├── WebUtil.java
    └── Util.java

src/main/resources/
├── application.yml
├── database.yml
├── aws.yml
├── mybatis/
│   ├── configuration.xml
│   └── mappers/
│       ├── account.xml
│       ├── board.xml
│       ├── common.xml
│       └── hidden.xml
├── templates/             # Thymeleaf 템플릿
│   ├── layout/            # 마스터 레이아웃
│   ├── fragment/          # 공통 프래그먼트
│   └── page/              # 개별 페이지
└── static/
    ├── css/
    ├── js/
    └── img/
```

---

## 3. 페이지 및 라우팅

### 웹 페이지 (WebController)

| 경로 | 데스크탑 | 모바일 | 파라미터 | 설명 |
|------|----------|--------|----------|------|
| `/` | home.html | - | - | 메인 홈 |
| `/m` | - | m_home.html | - | 모바일 홈 |
| `/introduce` | introduce.html | - | `type`, `idx` | 교회 소개 |
| `/m/introduce` | - | m_introduce.html | `type`, `idx` | 모바일 소개 |
| `/department` | department.html | - | `type`, `idx` | 부서 소개 |
| `/m/department` | - | m_department.html | `type`, `idx` | 모바일 부서 |
| `/worship` | worship.html | - | `type`, `idx` | 예배/설교 |
| `/m/worship` | - | m_worship.html | `type`, `idx` | 모바일 예배 |
| `/community` | community.html | - | `type`, `idx` | 커뮤니티 |
| `/m/community` | - | m_community.html | `type`, `idx` | 모바일 커뮤니티 |
| `/assembly` | assembly.html | - | - | 총회 |
| `/m/assembly` | - | m_assembly.html | - | 모바일 총회 |
| `/hidden/prayer/{group_key}` | prayer.html | - | group_key | 기도 요청 (로그인 필요) |

> **참고:** 데스크탑/모바일 각각 별도 페이지로 분리되어 있음. Nuxt3 마이그레이션 시 반응형으로 통합 가능.

---

## 4. API 엔드포인트

### 인증 API

| 메서드 | 경로 | 인증 | 설명 |
|--------|------|------|------|
| POST | `/auth/login` | - | AJAX 로그인 (JWT 발급) |
| POST | `/api/v1/public/account/register` | - | 회원가입 |

### 게시판 API - 설교 (Worship)

| 메서드 | 경로 | 인증 | 설명 |
|--------|------|------|------|
| GET | `/api/v1/user/board/worship` | 필요 | 설교 목록 (페이징) |
| GET | `/api/v1/user/board/worship/{idx}` | 필요 | 설교 상세 |
| POST | `/api/v1/user/board/worship` | 필요 | 설교 등록 |
| PUT | `/api/v1/user/board/worship` | 필요 | 설교 수정 |
| DELETE | `/api/v1/user/board/worship/{idx}` | 필요 | 설교 삭제 (소프트) |

**설교 유형 (worship_type):** sunday, friday, wednesday, dawn, praise, etc., home-worship
**카테고리 (category):** head-pastor, old-pastor, sub-pastor

### 게시판 API - 커뮤니티 (Community)

| 메서드 | 경로 | 인증 | 설명 |
|--------|------|------|------|
| GET | `/api/v1/user/board/community` | 필요 | 커뮤니티 목록 (페이징) |
| GET | `/api/v1/user/board/community/{idx}` | 필요 | 커뮤니티 상세 |
| POST | `/api/v1/user/board/community` | 필요 | 커뮤니티 등록 (multipart) |
| POST | `/api/v1/user/board/community/edit` | 필요 | 커뮤니티 수정 |
| DELETE | `/api/v1/user/board/community/{idx}` | 필요 | 커뮤니티 삭제 |

**카테고리:** notice, paper, photo, file, edit, mp3

### 기도 요청 API

| 메서드 | 경로 | 인증 | 설명 |
|--------|------|------|------|
| GET | `/api/v1/user/hidden/prayer/{group_key}` | 필요 | 기도 요청 목록 |
| POST | `/api/v1/user/hidden/prayer/content` | 필요 | 기도 요청 등록 |

### 공통 API

| 메서드 | 경로 | 인증 | 설명 |
|--------|------|------|------|
| GET | `/api/v1/public/common/main-banners` | - | 메인 배너 목록 |
| GET | `/api/v1/public/common/histories` | - | 교회 역사 |

---

## 5. 데이터베이스

**DB:** AWS RDS MySQL (ap-northeast-2)
**Host:** database-1.climzdtlgsg7.ap-northeast-2.rds.amazonaws.com:3306
**Database명:** wj

### 테이블 구조 (MyBatis 매퍼 기반 추정)

```sql
-- 사용자
wj_user (
  user_idx, user_id, user_name, user_email, user_phone,
  user_hash, user_salt, created_date
)

-- 설교 게시판
wj_board_worship (
  board_idx, created_user_idx, category, worship_type,
  worship_date, title, verse, host_name, content,
  is_deleted, created_date, updated_date
)

-- 커뮤니티 게시판
wj_board (
  board_idx, created_user_idx, category, title, content,
  is_deleted, created_date, updated_date
)

-- 첨부 파일
board_files (
  file_id, board_idx, file_type, aws_file_name,
  origin_file_name, ord, url, thumb_url,
  is_deleted, created_date
)

-- 기도 그룹
prayer_group (group_key, group_name)

-- 기도 요청
prayer (prayer_id, group_key, content, user_idx, created_date)

-- 메인 배너
main_banner (...)

-- 교회 역사
church_history (...)
```

---

## 6. 보안 / 인증

- **방식:** JWT (JJWT 0.7.0)
- **로그인 엔드포인트:** `/auth/login` (AJAX POST)
- **토큰 위치:** Authorization 헤더 (Bearer)
- **Access Token 유효기간:** 4시간 (14400초)
- **Refresh Token 유효기간:** 8시간 (28800초)
- **세션:** STATELESS
- **CSRF:** 비활성화
- **비밀번호 해싱:** MD5 + Salt (20자 랜덤 솔트)

**공개 엔드포인트:** `/api/v1/public/**`
**보호 엔드포인트:** `/api/v1/user/**` (JWT 필요)

---

## 7. AWS 인프라

| 서비스 | 설정 |
|--------|------|
| Elastic Beanstalk | wjhome-env, ap-northeast-2 |
| RDS MySQL | ap-northeast-2, database-1.climzdtlgsg7.ap-northeast-2.rds.amazonaws.com |
| S3 | kor-wj-bucket, ap-northeast-2 |

**S3 디렉토리 구조:**
- `images/files/` - 원본 파일
- `images/thumbs/` - 썸네일 (400x400 자동 생성)

---

## 8. 프론트엔드 구성

### 라이브러리
- **jQuery** 3.6.0
- **RequireJS** - 모듈 로더
- **Swiper.js** - 캐러셀/슬라이더
- **Font Awesome** - 아이콘

### JS 모듈 구조

| 모듈 | 파일 |
|------|------|
| 공통 | ajax.js, ajaxRequests.js, gVar.js, header.js, init.js, loginPopup.js, paging.js, userInfo.js, util.js |
| 홈 | homeInit.js, homeMain.js |
| 예배 | worshipInit.js, worshipMain.js |
| 커뮤니티 | communityInit.js, communityMain.js |
| 소개 | introduceInit.js, introduceMain.js |
| 부서 | departmentInit.js, departmentMain.js |
| 기도 | prayerInit.js, prayerMain.js |
| 총회 | assemblyInit.js |

### CSS 구조

| 범위 | 파일 |
|------|------|
| 공통 | reset.css, common.css, header.css, footer.css, style.css |
| 페이지별 | home.css, introduce.css, worship.css, community.css, assembly.css, department.css, board.css |
| 모바일 | m_home.css, m_introduce.css, m_worship.css, m_community.css, m_department.css, m_board.css |

> **현재 문제:** 데스크탑/모바일 CSS/JS/HTML 모두 별도 파일로 관리 → 유지보수 부담 큼

---

## 9. 기능 요약

### 비로그인 (공개) 기능
- 메인 홈 (배너 캐러셀, 최근 설교, 공지사항, 사진 갤러리 미리보기)
- 교회 소개 (교회 정보, 역사, 비전)
- 예배/설교 아카이브 (분류별 필터링, 페이지네이션)
- 커뮤니티 (공지, 주보, 사진, 파일, 편집, MP3)
- 부서 소개 (영아부, 초등부, 청소년부, 청년부, 여성부, 남성부)
- 총회 뉴스

### 로그인 필요 기능
- 기도 요청 조회/등록 (`/hidden/prayer/{group_key}`)
- 설교 게시판 CRUD (관리자)
- 커뮤니티 게시판 CRUD + 파일 업로드 (관리자)

---

## 10. Vue3 + Nuxt3 마이그레이션 포인트

### 변경 필요 사항

| 항목 | 현재 | 마이그레이션 후 |
|------|------|----------------|
| 렌더링 | Thymeleaf SSR | Nuxt3 SSR/SSG |
| 반응형 | 데스크탑/모바일 별도 페이지 | Vue3 반응형 레이아웃 통합 |
| 상태 관리 | 없음 (jQuery) | Pinia |
| HTTP 클라이언트 | jQuery AJAX | useFetch / $fetch |
| 인증 | JWT (쿠키 or 로컬스토리지) | Nuxt 미들웨어 + JWT |
| CSS | 순수 CSS | Tailwind CSS 또는 SCSS |
| 컴포넌트 | 없음 (fragment 방식) | Vue SFC 컴포넌트 |
| 파일 업로드 | Spring Multipart → S3 | API Route or 기존 API 유지 |
| 이미지 처리 | 서버사이드 (imgscalr) | 기존 API 유지 또는 Nuxt Image |

### 확정된 아키텍처: Option A (프론트엔드만 교체)

```
사용자 → Vercel (Nuxt3) → api.woojeong.or.kr (Spring Boot EB) → RDS / S3
```

- **프론트엔드:** Nuxt3 → Vercel 배포 (무료 플랜, HTTPS 자동 적용)
- **백엔드:** Spring Boot → Elastic Beanstalk 유지 (API 서버로만 사용)
- **DB/Storage:** RDS MySQL, S3 그대로 유지
- **도메인:** `woojeong.or.kr` → Vercel 연결 (DNS 변경), `api.woojeong.or.kr` → Elastic Beanstalk
- **프로젝트 구조:** 별도 폴더 분리 (`wj` 백엔드 / `wj-front` 프론트엔드)

**Spring Boot 변경 사항:**
- CORS에 Vercel 도메인 추가
- Thymeleaf 및 정적 파일(CSS/JS) 제거 (프론트 완성 후)
- `web/WebController.java` 제거

### 마이그레이션 순서

1. `wj-front` Nuxt3 프로젝트 초기화
2. 공통 레이아웃/헤더/푸터 컴포넌트 구현
3. 공개 페이지부터 구현 (홈, 소개, 예배, 커뮤니티, 부서, 총회)
4. 인증 (로그인/JWT) 구현
5. 보호 페이지 (기도 요청) 구현
6. 관리자 기능 구현 (게시판 CRUD)
7. Vercel 배포 + 도메인 연결
8. Spring Boot Thymeleaf 제거 및 정리

---

## 11. 참고 - 외부 연동

| 서비스 | 용도 |
|--------|------|
| Naver API | SEO/지도 등 (client-id: fyti1ssu8d) |
| AWS S3 | 이미지/파일 업로드 (kor-wj-bucket) |
| AWS RDS | MySQL 데이터베이스 |

---

*최초 작성: 2026-03-28 / 마이그레이션 방향 확정: 2026-03-28*
