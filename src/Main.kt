import com.google.gson.Gson
import java.io.File
import java.math.BigDecimal
import java.text.Normalizer
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.io.BufferedWriter
import java.io.Writer




fun main(args: Array<String>){
    println("hello")
    emojiCollect()

}

fun emojiCollect(){
    var  masterList = ArrayList<Emoji>()
    // pass the path to the file as a parameter
    val file = File("C:\\Users\\gaine\\Downloads\\kotlinsourcecode\\EmojiExtract\\assets\\emojis.txt");
    val scanner = Scanner(file);
    var groupName = ""
    var subGroupName = ""
    var semiIndex = 43
    var poundIndex = 65
    var lineLength = 0
    var tempLine = ""
    var block1 = ""
    var block2 = ""
    var block3 = ""
    var block2Max = 16
    var startOfDescriptionIndex = 0

    while (scanner.hasNextLine()){
        //grab the line
        tempLine = scanner.nextLine()
        println(tempLine)
        lineLength = tempLine.length
        println("Line Length: ${tempLine.length}")
        //check if group
        if(tempLine.startsWith("# group")){
            groupName = tempLine.substring(8, lineLength).trim()
            println("Group set: ${groupName}")
        }
        //check if subgroup
        if(tempLine.startsWith("# subgroup")){
            subGroupName = tempLine.substring(12,lineLength)
            println("SubGroup set: ${subGroupName}")
        }

        //if not a group declaration, proceed
        if(tempLine.startsWith("1")) {
            println("Emoji object creation start")

            /*
            grab middle block first to avoid unneccessary code actions if it is duplicate

            block2 = tempLine.substring(semiIndex + 1, poundIndex).trim()


            //consider if it is duplicate, if not, continiue with code
           if (block2.length < block2Max) {
            unused for now
            */

                block1 = tempLine.substring(0, semiIndex).trim()
                println("block1: $block1")

                block3 = tempLine.substring(poundIndex + 1, lineLength).trim()
                println("block3: $block3")
                val tempArray = block3.toCharArray()

                for (i in 0 until block3.length) {
                    if (tempArray.get(i).isLetter()) {
                        startOfDescriptionIndex = i
                        println("Start of Description index: $startOfDescriptionIndex")
                        break
                    }
                }
                block3 = block3.substring(startOfDescriptionIndex)
                val emoji = Emoji(group = groupName, subGroup = subGroupName, emojiString = turnStringListIntoCodePointArray(block1.split(" ")), description = block3)
                masterList.add(emoji)
                println("Emoji object creation end")
           // }
        }
    }
    for(i in masterList.indices){
        masterList.get(i).printData()
    }
    println("total emojis: ${masterList.count()}")
    val gson = Gson()
    val json = gson.toJson(masterList)
    writeJSONfile(json)
    writeSubGroupFile(masterList)
}

fun writeSubGroupFile(masterList: ArrayList<Emoji>) {
    val subList = masterList.asSequence().map{it.subGroup}.distinct().toList()
    val gson = Gson()
    val json = gson.toJson(subList)
    BufferedWriter(OutputStreamWriter(
            FileOutputStream("C:\\Users\\gaine\\Downloads\\kotlinsourcecode\\EmojiExtract\\assets\\sub_groups.txt"), "utf-8")).use { writer -> writer.write(json) }

}


data class Emoji(val group: String, val subGroup: String, val emojiString: String, val description: String){
    fun printData(){
        println()
        print(group)
        print("\t\t")
        print(subGroup)
        print("\t\t")
        println(emojiString)
        println(description)
        println()
    }

}

fun turnStringListIntoCodePointArray(codePoints: List<String>) : String{
    val array = ArrayList<Int>()
    for (i in codePoints.indices){
        val codepoint = Integer.parseInt(codePoints.get(i), 16)
        array.add(codepoint)
    }
    return String(array.toIntArray(), 0, array.size)

}

fun writeJSONfile(json: String){
    BufferedWriter(OutputStreamWriter(
            FileOutputStream("C:\\Users\\gaine\\Downloads\\kotlinsourcecode\\EmojiExtract\\assets\\json.txt"), "utf-8")).use { writer -> writer.write(json) }
}
