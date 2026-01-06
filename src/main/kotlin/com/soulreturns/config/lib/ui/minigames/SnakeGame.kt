package com.soulreturns.config.lib.ui.minigames

import kotlin.random.Random

/**
 * Simple Snake game model used by the config GUI's Minigames tab.
 *
 * This class is intentionally UI-agnostic: it tracks grid state,
 * movement, food, and scoring, while the screen decides how to
 * render the grid inside a rectangle.
 */
data class GridPos(val x: Int, val y: Int)

class SnakeGame(
    val columns: Int = 24,
    val rows: Int = 18,
    private val tickMillis: Long = 150L,
) {
    enum class Direction { UP, DOWN, LEFT, RIGHT }

    private val random = Random.Default
    private val body: ArrayDeque<GridPos> = ArrayDeque()

    private var direction: Direction = Direction.RIGHT
    private var pendingDirection: Direction? = null
    private var lastUpdateTime: Long = 0L

    var food: GridPos = GridPos(0, 0)
        private set

    var isAlive: Boolean = true
        private set

    var score: Int = 0
        private set

    /** Public view of the snake segments from tail to head. */
    val segments: List<GridPos>
        get() = body.toList()

    /** Reset the game to a fresh state. */
    fun reset(nowMillis: Long = System.currentTimeMillis()) {
        body.clear()
        val startX = columns / 2
        val startY = rows / 2

        // Start with a short horizontal snake moving to the right.
        body.addLast(GridPos(startX - 1, startY))
        body.addLast(GridPos(startX, startY))

        direction = Direction.RIGHT
        pendingDirection = null
        score = 0
        isAlive = true
        lastUpdateTime = nowMillis
        spawnFood()
    }

    /**
     * Advance the game state at a fixed tick rate. Should be called
     * regularly from the render loop while the game is visible.
     */
    fun update(nowMillis: Long) {
        if (!isAlive) return

        // Lazily initialize if never updated before.
        if (lastUpdateTime == 0L || body.isEmpty()) {
            reset(nowMillis)
            return
        }

        if (nowMillis - lastUpdateTime < tickMillis) return
        lastUpdateTime = nowMillis

        // Apply any pending direction change that isn't a 180° turn.
        pendingDirection?.let { newDir ->
            if (!isOpposite(newDir, direction)) {
                direction = newDir
            }
            pendingDirection = null
        }

        val head = body.last()
        val next = when (direction) {
            Direction.UP -> GridPos(head.x, head.y - 1)
            Direction.DOWN -> GridPos(head.x, head.y + 1)
            Direction.LEFT -> GridPos(head.x - 1, head.y)
            Direction.RIGHT -> GridPos(head.x + 1, head.y)
        }

        // Wall collision ends the game.
        if (next.x !in 0 until columns || next.y !in 0 until rows) {
            isAlive = false
            return
        }

        // Self-collision ends the game.
        if (body.any { it.x == next.x && it.y == next.y }) {
            isAlive = false
            return
        }

        body.addLast(next)

        if (next.x == food.x && next.y == food.y) {
            score += 1
            spawnFood()
        } else {
            // Move forward: drop tail.
            body.removeFirst()
        }
    }

    /** Queue a direction change, applied on the next tick. */
    fun changeDirection(newDirection: Direction) {
        if (!isAlive) return
        pendingDirection = newDirection
    }

    private fun isOpposite(a: Direction, b: Direction): Boolean {
        return (a == Direction.UP && b == Direction.DOWN) ||
            (a == Direction.DOWN && b == Direction.UP) ||
            (a == Direction.LEFT && b == Direction.RIGHT) ||
            (a == Direction.RIGHT && b == Direction.LEFT)
    }

    private fun spawnFood() {
        // If the snake fills the board, just pin food to the head.
        if (body.size >= columns * rows) {
            food = body.last()
            return
        }

        var attempts = 0
        while (attempts < 1000) {
            val x = random.nextInt(columns)
            val y = random.nextInt(rows)
            val candidate = GridPos(x, y)
            if (body.none { it.x == candidate.x && it.y == candidate.y }) {
                food = candidate
                return
            }
            attempts++
        }

        // Fallback: place food on the head if random placement fails.
        food = body.last()
    }
}
