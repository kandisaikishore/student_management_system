# Scalability baseline

Scale the application based on actual usage and measured bottlenecks. Avoid distributed infrastructure until measurements justify it.

## Priorities
- Profile user workflows and hot paths before optimizing or adding replicas.
- Bound input, request size, concurrency, retries, connection pools, and external dependency timeouts.
- Paginate long lists and keep data access scoped; optimize database queries/indexes based on query plans.
- Use transactions and idempotency for related or retryable state-changing operations.
- Keep service instances stateless where practical; externalize shared state before enabling horizontal scaling.
- Add health/readiness checks, structured logs, request IDs, metrics, graceful shutdown, and rollback procedures for hosted services.
- Keep secrets and sensitive user data out of source control and logs.
- Add build, unit, integration, dependency/security, and deployment smoke checks suited to the existing stack.

## Load validation
Test normal, peak, burst, and dependency-failure scenarios with representative data. Measure throughput, p95/p99 latency, error rate, CPU/memory, database wait time, and queue lag where relevant.

## Release gates
- [ ] Input validation, authorization, and data isolation are tested.
- [ ] Retries are bounded and mutations idempotent where needed.
- [ ] Migrations, backup/restore, and rollback are validated for persistent data.
- [ ] Capacity assumptions and load-test results are recorded.

This checklist is a plan, not evidence of completed load testing or production scaling.