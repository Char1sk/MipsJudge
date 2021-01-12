import java.util.ArrayList;
import java.util.HashMap;

public class Assembler {
    // Classes
    enum InstructionType {
        RType,
        IType,
        JTYpe;
    }
    class Instruction {
        protected InstructionType type;
        protected String content;
        protected String name;
    }
    class RTypeInstruction extends Instruction {
        protected String opcode;    //  6bit, 31:26
        protected String rs;        //  5bit, 25:21
        protected String rt;        //  5bit, 20:16
        protected String rd;        //  5bit, 15:11
        protected String shamt;     //  5bit, 10: 6
        protected String funct;     //  6bit,  5: 0
    }
    class ITypeInstruction extends Instruction {
        protected String opcode;    //  6bit, 31:26
        protected String rs;        //  5bit, 25:21
        protected String rt;        //  5bit, 20:16
        protected String offset;    // 16bit, 15: 0
    }
    class JTypeInstruction extends Instruction {
        protected String opcode;    //  6bit, 31:26
        protected String immediate; // 24bit, 25: 0
    }
    // Attributes
    // instructions related
    protected ArrayList<String> instructions;
    protected int totalNumber;
    protected int nowNumber;
    // table related
    protected static HashMap<String, String> registerTable;
    protected static HashMap<String, InstructionType> typeTable;
    protected static HashMap<String, String> opcodeType;
    protected static HashMap<String, String> functType;
}

