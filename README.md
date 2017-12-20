# Ticket Challenge

## Preliminary Thoughts
- Need to classify "high performance". Largest stadium in the world has a capacity of [150,000](https://www.worldatlas.com/articles/50-largest-stadiums-in-the-world.html). Without further investigation, serving 150,000 requests/sec seems to be a reasonable upper performance bound. Looking at some [benchmarking data](https://dzone.com/articles/benchmarking-high-concurrency-http-servers-on-the) we are on the edge of what is possible. The machine in those benchmarks is relatively small, and 150,000 is relatively high, so a Java app should "just work" without any special performance considerations, at least to start.
- For high-availability it'd be nice to make the app stateless. We should be fine getting the above throughput with a standard relational database (e.g. MySQL or Postgres), so we will have an in-memory repository here that we know could be replaced easily without much concern around performance.

