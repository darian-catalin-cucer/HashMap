//HashMap implementation in Kotlin
class ExplainedHashMap<K, V> {
    private val DEFAULT_CAPACITY = 16
    private var size = 0
    private var loadFactor = 0.75f
    private var threshold = (DEFAULT_CAPACITY * loadFactor).toInt()
    private var buckets = Array<Bucket<K, V>>(DEFAULT_CAPACITY) { Bucket() }

    private class Bucket<K, V> {
        var key: K? = null
        var value: V? = null
        var next: Bucket<K, V>? = null
    }

    fun put(key: K, value: V) {
        var index = getIndex(key)
        var bucket = buckets[index]

        while (bucket.key != null) {
            if (bucket.key == key) {
                bucket.value = value
                return
            }
            bucket = bucket.next ?: Bucket<K, V>().also { bucket.next = it }
        }

        bucket.key = key
        bucket.value = value
        size++
        if (size >= threshold) {
            resize()
        }
    }

    fun get(key: K): V? {
        var index = getIndex(key)
        var bucket = buckets[index]

        while (bucket.key != null) {
            if (bucket.key == key) {
                return bucket.value
            }
            bucket = bucket.next ?: return null
        }
        return null
    }

    private fun getIndex(key: K): Int {
        return key.hashCode() % buckets.size
    }

    private fun resize() {
        val temp = buckets
        buckets = Array(buckets.size * 2) { Bucket() }
        threshold = (buckets.size * loadFactor).toInt()
        size = 0

        temp.forEach { bucket ->
            var b: Bucket<K, V>? = bucket
            while (b != null) {
                put(b.key!!, b.value!!)
                b = b.next
            }
        }
    }

    fun size() = size
}
