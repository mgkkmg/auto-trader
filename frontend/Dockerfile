# 기본 의존성 설치
FROM node:20.14.0-alpine AS base

# Install dependencies only when needed
FROM base AS deps
RUN apk add --no-cache libc6-compat
WORKDIR /app

# 노드 패키지 설치
COPY auto-trader/package.json auto-trader/yarn.lock ./
RUN yarn install --frozen-lockfile --production=false
RUN yarn cache clean

# 프로젝트 빌드
FROM base AS builder
WORKDIR /app
COPY --from=deps /app/node_modules ./node_modules
COPY auto-trader .

# next.config.js 확인
RUN cat next.config.js

# 빌드 시도 및 로그 출력
RUN yarn build || (echo "Build failed. Yarn logs:" && yarn logs && exit 1)

# 빌드 결과 확인
RUN ls -la .next

# 프로젝트 실행
FROM base AS runner
WORKDIR /app

# 컨테이너 환경에 시스템 사용자 추가
RUN addgroup --system --gid 1001 nodejs
RUN adduser --system --uid 1001 nextjs

# 빌드 단계에서 생성된 결과만 복사
# next.config.js 에 standalone 디렉토리 설정 추가 -> standalone 파일 사용하여 프로젝트 실행
COPY --from=builder /app/public ./public
COPY --from=builder --chown=nextjs:nodejs /app/.next/standalone ./
COPY --from=builder --chown=nextjs:nodejs /app/.next/static ./.next/static

# 포트 노출
EXPOSE 3000

# Next.js 애플리케이션 시작
# standalone 으로 나온 결과값은 노드 자체로만 실행 가능
CMD [ "node", "server.js" ]