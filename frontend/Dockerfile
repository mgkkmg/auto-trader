# 기본 의존성 설치
FROM node:20.14.0-alpine AS base

# Install dependencies only when needed
FROM base AS deps
RUN apk add --no-cache libc6-compat
WORKDIR /usr/src/app

# 노드 패키지 설치
# --frozen-lockfile 옵션으로 yarn.lock 파일에 작성한 그대로 패키지를 설치하도록
# 캐시 삭제해서 1차 이미지 경량화
COPY package.json yarn.lock ./
RUN yarn --frozen-lockfile --production;
RUN rm -rf ./.next/cache

# 프로젝트 빌드
FROM base AS builder
WORKDIR /usr/src/app
COPY --from=deps /usr/src/app/node_modules ./node_modules
COPY . .

RUN yarn build

# 프로젝트 실행
FROM base AS runner
WORKDIR /usr/src/app

# 컨테이너 환경에 시스템 사용자 추가
RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

# 빌드 단계에서 생성된 결과만 복사
# next.config.mjs 에 standalone 디렉토리 설정 추가 -> standalone 파일 사용하여 프로젝트 실행
COPY --from=builder /usr/src/app/public ./public
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /usr/src/app/.next/static ./.next/static

# 포트 노출
EXPOSE 3000

# Next.js 애플리케이션 시작
# standalone 으로 나온 결과값은 노드 자체로만 실행 가능
CMD [ "node", "server.js" ]