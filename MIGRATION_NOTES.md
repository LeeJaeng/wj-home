# Spring Boot 마이그레이션 변경 사항

## Nuxt3 프론트엔드 전환을 위한 백엔드 수정 내역

### 1. CORS 설정 업데이트 (`SecurityConfiguration.java`)

추가된 허용 Origin:
- `https://woojeong.vercel.app` — Vercel 배포 도메인
- `https://wj-front.vercel.app` — 프리뷰 도메인
- `http://localhost:3000` — 로컬 Nuxt3 개발
- `http://127.0.0.1:3000`

> **주의**: Vercel 실제 배포 도메인 확정 후 업데이트 필요

### 2. 공개 게시판 API 추가 (`BoardPublicRestController.java`)

비로그인 사용자도 게시판 조회 가능하도록 새 컨트롤러 추가:

| 메서드 | 경로 | 설명 |
|--------|------|------|
| GET | `/api/v1/public/board/worship` | 설교 목록 (인증 불필요) |
| GET | `/api/v1/public/board/worship/{idx}` | 설교 상세 (인증 불필요) |
| GET | `/api/v1/public/board/community` | 커뮤니티 목록 (인증 불필요) |
| GET | `/api/v1/public/board/community/{idx}` | 커뮤니티 상세 (인증 불필요) |

기존 `/api/v1/user/board/**` 엔드포인트는 유지 (POST/PUT/DELETE - 관리자 전용)

### 3. BoardService 메서드 추가

공개 API 전용 메서드 추가 (user_idx = 0L 로 처리):
- `getWorshipBoardListPublic(type, page, init)`
- `getWorshipBoardPublic(idx)`
- `getCommunityBoardListPublic(type, page, init)`
- `getCommunityBoardPublic(idx)`

### 4. TODO: 프론트 완성 후 처리할 사항

- [ ] `WebController.java` 제거 (Thymeleaf 라우팅 불필요)
- [ ] `build.gradle`에서 Thymeleaf 의존성 제거
- [ ] `src/main/resources/templates/` 폴더 제거
- [ ] `src/main/resources/static/` 폴더 제거 (CSS/JS/이미지)
- [ ] Vercel 실제 도메인으로 CORS 업데이트

### 5. 도메인 DNS 설정 계획

```
woojeong.or.kr     → Vercel (A 레코드 또는 CNAME)
api.woojeong.or.kr → Elastic Beanstalk (CNAME)
```
