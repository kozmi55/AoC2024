import java.io.File
import java.io.InputStream

fun day09part1() {
    val inputStream: InputStream = File("${INPUT_PATH}input9.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    var fileId = 0L
    val diskSpace = lineList.first().toCharArray().mapIndexed { index, c ->
        if (index % 2 == 0) {
            val file = FileSpace(fileId, c.digitToInt().toLong())
            fileId++
            file
        } else {
            FreeSpace(c.digitToInt().toLong())
        }
    }

    val filesReversed = diskSpace.filterIsInstance<FileSpace>().reversed().toMutableList()
    val orderedDiskSpace = mutableListOf<FileSpace>()

    val processedIds = mutableSetOf<Long>()
    var inProgressId = -1L
    diskSpace.forEachIndexed { _, space ->
        if (space is FileSpace) {
            if (!processedIds.contains(space.id) && inProgressId != space.id) {
                processedIds.add(space.id)
                orderedDiskSpace.add(space)
            }
        } else {
            var blocks = space.blocks
            var next = filesReversed.first()
            if (processedIds.contains(next.id)) return@forEachIndexed
            while (blocks >= next.blocks) {
                blocks -= next.blocks
                orderedDiskSpace.add(next)
                filesReversed.removeFirst()
                processedIds.add(next.id)
                next = filesReversed.first()
                if (processedIds.contains(next.id)) return@forEachIndexed
            }
            if (blocks > 0) {
                val remaining = FileSpace(next.id, next.blocks - blocks)
                val toInsert = FileSpace(next.id, blocks)
                filesReversed[0] = remaining
                inProgressId = remaining.id
                orderedDiskSpace.add(toInsert)
            }
        }
    }

    val resultList = mutableListOf<Long>()
    orderedDiskSpace.forEach { fileSpace ->
        repeat(fileSpace.blocks.toInt()) {
            resultList.add(fileSpace.id)
        }
    }
    val result = resultList.mapIndexed { index, id -> index * id }.sum()
    println(result)
}

fun day09part2() {
    val inputStream: InputStream = File("${INPUT_PATH}input9.txt").inputStream()

    val lineList = inputStream.bufferedReader().readLines()

    var fileId = 0L
    val diskSpace = lineList.first().toCharArray().mapIndexed { index, c ->
        if (index % 2 == 0) {
            val file = FileSpace(fileId, c.digitToInt().toLong())
            fileId++
            file
        } else {
            FreeSpace(c.digitToInt().toLong())
        }
    }
        .filter { it.blocks != 0L }
        .toMutableList()

    val diskSpaceReversed = diskSpace.reversed()

    diskSpaceReversed.forEach { space ->
        if (space is FileSpace) {
            val freeIndex = diskSpace.indexOfFirst { it is FreeSpace && it.blocks >= space.blocks }
            val originalIndexOfFile = diskSpace.indexOfFirst { (it as? FileSpace)?.id == space.id }
            if (freeIndex != -1 && originalIndexOfFile > freeIndex) {
                val blocksAtIndex = diskSpace[freeIndex].blocks
                diskSpace[freeIndex] = space
                if (blocksAtIndex > space.blocks) {
                    diskSpace.add(freeIndex + 1, FreeSpace(blocksAtIndex - space.blocks))
                }
            }
        }
    }

    val resultList = mutableListOf<Long>()
    val processedIds = mutableSetOf<Long>()
    diskSpace.forEach { space ->
        if (space is FileSpace) {
            if (processedIds.contains(space.id)) {
                repeat(space.blocks.toInt()) {
                    resultList.add(0)
                }
            } else {
                processedIds.add(space.id)
                repeat(space.blocks.toInt()) {
                    resultList.add(space.id)
                }
            }
        } else {
            repeat(space.blocks.toInt()) {
                resultList.add(0)
            }
        }
    }
    val result = resultList.mapIndexed { index, id -> index * id }.sum()
    println(result)
}

open class Space(val blocks: Long)

class FileSpace(val id: Long, blocks: Long) : Space(blocks)

class FreeSpace(blocks: Long) : Space(blocks)