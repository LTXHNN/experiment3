package experiment3;
import java.util.*;
/**
 * @author 李天翔
 * @date 2022/05/30
 **/
@SuppressWarnings({"all"})
public class LR_1 {
    private static String[] grammar = {"E->E+T", "E->T", "T->T*F", "T->F", "F->(E)", "F->i"};
    static HashSet<Character> vtSet = new HashSet<>();//终结符
    static HashSet<Character> vnSet = new HashSet<>();//非终结符
    static char start;//保存开始符号
    static Stack<Integer> state = new Stack<>();//状态栈
    static Stack<Character> characters = new Stack<>();//符号栈
    static String input;//输入字符串
    public static ArrayList<Step> stepsList = new ArrayList<>();//保存步骤
    static String action[][] = {
            {"i", "+", "*", "(", ")", "#"},
            {"s5", "", "", "s4", "", ""},
            {"", "s6", "", "", "", "acc"},
            {"", "r2", "s7", "", "r2", "r2"},
            {"", "r4", "r4", "", "r4", "r4"},
            {"s5", "", "", "s4", "", ""},
            {"", "r6", "r6", "", "r6", "r6"},
            {"s5", "", "", "s4", "", ""},
            {"s5", "", "", "s4", "", ""},
            {"", "s6", "", "", "s11", ""},
            {"", "r1", "s7", "", "r1", "r1"},
            {"", "r3", "r3", "", "r3", "r3"},
            {"", "r5", "r5", "", "r5", "r5"}};
    static int goTo[][] = {
            {1, 2, 3},
            {-1, -1, -1},
            {-1, -1, -1},
            {-1, -1, -1},
            {8, 2, 3},
            {-1, -1, -1},
            {-1, 9, 3},
            {-1, -1, 10},
            {-1, -1, -1},
            {-1, -1, -1},
            {-1, -1, -1},
            {-1, -1, -1}
    };

    /**
     * 用来得到终结符集和非终结符集
     */
    public static void init() {
        start = grammar[0].charAt(0);
        //生成终结符和非终结符集
        for (int i = 0; i < grammar.length; i++) {
            char[] temp = grammar[i].replace("->", "").replace("|", "").toCharArray();//去除->

            for (int j = 0; j < temp.length; j++) {
                if (temp[j] >= 'A' && temp[j] <= 'Z') {
                    vnSet.add(temp[j]);//大写字母为非终结符
                } else {
                    vtSet.add(temp[j]);
                }
            }
        }

        System.out.println(vnSet + "***" + vnSet);
    }
    /**
     * 得到action表中对应动作
     * @param topState
     * @param in 待输入符号
     * @return
     */
    public static String getAction(int topState, char in) {
        String s = "";
        for (int i = 0; i < action[0].length; i++) {
            if (action[0][i].equals(in + "")) {
                s = action[topState + 1][i];
                break;
            }
        }
        return s;
    }

    /**
     * 得到状态栈全部符号
     * @return
     */
    public static String getStateStack() {
        String s = "";
        for (Integer i : state) {
            s += i;
        }

        return s;
    }

    /**
     * 得到符号栈全部符号
     * @return
     */
    public static String getCharStack() {
        String s = "";
        for (Character c : characters) {
            s += c;
        }

        return s;
    }

    /**
     * 得到goto表中的状态
     * @param state 当前栈顶状态
     * @param c 当前非终结符
     * @return
     */
    public static int getGoto(int state, char c) {
        int t = -1;
        if (c == 'E') {
            t = goTo[state][0];
        } else if (c == 'T') {
            t = goTo[state][1];
        } else if (c == 'F') {
            t = goTo[state][2];
        }
        return t;
    }

    /**
     * 执行分析操作
     */
    public static void analyze() {
        state.push(0);
        characters.push('#');
        char[] inputCh = input.toCharArray();
        Step step1;
        int step = 1;
        int topState = state.peek();
        char nowInput;
        int point = 0;
        while (true) {
            nowInput = inputCh[point];
            String ac = getAction(topState, nowInput);
            if (ac.equals("acc")) {
                step1 = new Step(step, getStateStack(), getCharStack(), input.substring(point), "Acc: 分析成功");
                stepsList.add(step1);
                step++;
                System.out.println("分析结束");
                return;
            }
            char sr = ac.charAt(0);//保存移进或归约符号
            int nowState = Integer.parseInt(ac.substring(1));//保存状态
            if (sr == 's') {//移进操作
                String pro = "ACTION[" + topState + "," + nowInput + "]" + "=S" + nowState + ",状态" + nowState + "入栈";
                step1 = new Step(step, getStateStack(), getCharStack(), input.substring(point), pro);
                stepsList.add(step1);
                step++;
                characters.push(nowInput);
                state.push(nowState);
                topState = state.peek();
                point++;
            } else {//归约操作
                int len = grammar[nowState - 1].length() - 3;//通过查找产生式得到对应长度用于符号栈出栈
                char vn = grammar[nowState - 1].charAt(0);//得到要归约成的非终结符
                String gra = grammar[nowState - 1];
                String st = getStateStack();
                String ct = getCharStack();
                for (int i = 0; i < len; i++) {
                    state.pop();
                    characters.pop();
                }
                characters.push(vn);
                topState = state.peek();
                int go = getGoto(topState, characters.peek());
                String pro = ac + ":" + gra + "归约,GOTO(" + topState + "," + vn + ")=" + go + "入栈";
                step1 = new Step(step, st, ct, input.substring(point), pro);
                state.push(go);
                topState = state.peek();
                stepsList.add(step1);
                step++;
            }
        }
    }

//    public static void main(String[] args) {
//        System.out.println("请输入字符串：");
//        Scanner scanner = new Scanner(System.in);
//        input = scanner.nextLine();
//        System.out.println("*************i+i*i 的 LR 分析过程*************");
//        System.out.println("步骤\t\t 状态栈\t\t 符号栈\t\t 输入串\t\t 动作说明");
//        analyze();
//        for (int i = 0; i < stepsList.size(); i++) {
//            System.out.println(stepsList.get(i));
//        }
//    }
}
