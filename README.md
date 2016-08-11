# FX Rate Batch
Spring batch job to fetch the latest FX rates for major currency pairs and persist them in a Cassandra data store, using our [fx-rate-importer](https://github.com/sharpecapital/fx-rate-importer) Scala library and [data-access-api](https://github.com/sharpecapital/data-access-api) abstraction library.
