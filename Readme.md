# SenCache : Java Caching System

## Project Overview
SenCache implements a caching system as an intermediate layer between an application and a file-based data source. It's designed to optimize read operations while ensuring data consistency for writes, using the Readers-Writers concurrency pattern.


## Architecture Decisions and Trade-offs

![High Level Architecture](architecture.png)

### 1. Data Format: JSON
- **Decision**: Use JSON for data storage
- **Pros**: Human-readable, flexible, widely supported
- **Cons**: Slower parsing compared to binary formats
- **Rationale**: Prioritized readability and flexibility over maximum performance because it's just a learning project

### 2. Cache Structure: LinkedHashMap
- **Decision**: Use LinkedHashMap as the base structure for the cache
- **Pros**: Built-in support for LRU ordering, good overall performance
- **Cons**: Requires additional synchronization for thread-safety
- **Rationale**: Balances ease of LRU implementation with good performance characteristics

### 3. Concurrency Model: Fair Access
- **Decision**: Implement fair access for readers and writers
- **Pros**: Balances read and write operations, prevents starvation
- **Cons**: May have slightly lower throughput
- **Rationale**: Chose fairness over maximum throughput to ensure both read and write operations are handled in an equitable manner

### 4. Cache Eviction Policy: LRU
- **Decision**: Implement Least Recently Used (LRU) eviction
- **Pros**: Effective for most use cases, simple to implement
- **Cons**: May not be optimal for all access patterns
- **Rationale**: LRU provides a good balance of simplicity and effectiveness for general use cases
- **Note**: As an extension, other eviction policies such as LFU or FIFO could be implemented later

### 5. Consistency Model: Write-through
- **Decision**: Update the file immediately on cache writes
- **Pros**: Ensures immediate consistency between cache and file
- **Cons**: May slow down write operations
- **Rationale**: Prioritized simplicity over write performance

### 6. Error Handling and Resilience
- [To be decided]

### 7. Scalability Considerations
- Use of interfaces for key components to allow future extensibility
- Modular design separating caching logic, data access, and concurrency control


## Future Improvements
- Support for different data sources (e.g., databases, external APIs)
- Distributed caching capabilities
- Additional eviction policies (e.g., LFU, FIFO)
- Enhanced monitoring and statistics
