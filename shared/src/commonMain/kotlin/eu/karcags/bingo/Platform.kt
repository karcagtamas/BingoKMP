package eu.karcags.bingo

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform