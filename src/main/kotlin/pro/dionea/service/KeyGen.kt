package pro.dionea.service

class KeyGen(private val size: Int) {
    private val sequence : List<Char> = ('a'..'z') + ('A'..'Z') + ('0'..'9')

    fun generate() : String {
        return (1 .. size)
            .map { _ -> kotlin.random.Random.nextInt(0, sequence.size) }
            .map(sequence::get)
            .joinToString("")
    }
}