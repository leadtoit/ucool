
/**
* Created by IntelliJ IDEA.
* User: czy-thinkpad
* Date: 11-12-17
* Time: ÏÂÎç10:49
* To change this template use File | Settings | File Templates.
*/
class ElementTest {
    def name
    def ark
}

def e = new ElementTest()
e.setName('gg')
println e.getName()


def map = [a:'b']
println map.a


// a readable puts chars into a CharBuffer and returns the count of chars added
def readable = { it.put("12 34".reverse()); 5} as Readable

// the Scanner constructor can take a Readable
def s = new Scanner(readable)
assert s.nextInt() != 43