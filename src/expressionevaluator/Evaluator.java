/**
 * This program evaluates mathematical expressions using the concept of stacks and operator precedence.
 * It supports addition, subtraction, multiplication, and division operations.
 * 
 * The 'Evaluator' class is the main class which processes the input string expression.
 * It uses two stacks, one for operands and one for operators.
 * 
 * The 'Operator' class is an abstract class that defines the structure for all mathematical operator classes.
 * It includes a 'priority' method to define precedence of the operators and an 'execute' method to execute the operation.
 *
 * The 'Operand' class represents numerical values in the expression. It provides methods to check if a token is an operand and to get the value of an operand.
 * 
 * 'BogusOp' is a special type of Operator used as a placeholder in the operator stack.
 * 
 * 'AddOp', 'SubOp', 'MulOp', and 'DivOp' are classes representing the four supported mathematical operations.
 * Each of them extends the 'Operator' class and defines the 'priority' and 'execute' methods.
*/

package expressionevaluator;

import java.util.*;

public class Evaluator
{
    private Stack<Operand> opdStack;
    private Stack<Operator> oprStack;
    private HashMap<String, Operator> operators;

    public Evaluator()
    {
        opdStack = new Stack<Operand>();
        oprStack = new Stack<Operator>();
        operators = new HashMap<String, Operator>();
        operators.put("#", new BogusOp());
        operators.put("+", new AddOp());
        operators.put("-", new SubOp());
        operators.put("*", new MulOp());
        operators.put("/", new DivOp());
    }

    public int eval(String expr)
    {
        String tok;
        oprStack.push(new BogusOp());
        // init stack - necessary with operator priority schema;
        // the priority of any operator in the operator stack other then
        // the usual operators - "+-*/" - should be less than the priority
        // of the usual operators oprStack.push(new Operator("#"));
        String delimiters = "+-*/%#! ";
        StringTokenizer st = new StringTokenizer(expr, delimiters, true);
        // the 3rd arg is true to indicate to use the delimiters as tokens, too
        // but we'll filter out spaces

        while (st.hasMoreTokens())
        {
            if (!(tok = st.nextToken()).equals(" "))
            {
                if (Operand.check(tok))
                {
                    opdStack.push(new Operand(tok));
                } else
                {
                    if (!Operator.check(tok))
                    {
                        System.out.println("*****invalid token******");
                        System.exit(1);
                    }
                    Operator newOpr = operators.get(tok); //POINT 1

                    if (newOpr.priority() != 1)
                    {
                        while ((oprStack.peek().priority() >= newOpr.priority()))
                        {
                            // note that when we eval the expression 1 - 2 we will
                            // push the 1 then the 2 and then do the subtraction operation
                            // This means that the first number to be popped is the
                            // second operand, not the first operand - see the following code
                            Operator oldOpr = oprStack.pop();
                            Operand op2 = opdStack.pop();
                            Operand op1 = opdStack.pop();
                            opdStack.push(oldOpr.execute(op1, op2));
                        }
                        oprStack.push(newOpr);
                    } else
                    {
                        oprStack.push(newOpr);
                    }
                }
            }
        }
        //evaluate last 2 operands in the stack before bogus operator
        while (oprStack.peek().priority() != 1)
        {
            Operator oldOpr = oprStack.pop();
            Operand op2 = opdStack.pop();
            Operand op1 = opdStack.pop();
            opdStack.push(oldOpr.execute(op1, op2));
        }
        return opdStack.pop().getValue();
    }
}

abstract class Operator
{

    public abstract int priority();

    public static boolean check(String tok)
    {
        if (tok.equals("+") || tok.equals("-") || tok.equals("*") || tok.equals("/"))
        {
            return true;
        } else
        {
            return false;
        }
    }

    public abstract Operand execute(Operand opd1, Operand opd2);

}

class Operand
{

    private int value;

    Operand(String tok)
    {
        value = Integer.parseInt(tok);
    }

    Operand(int value)
    {
        this.value = value;
    }

    public static boolean check(String tok)
    {
        return (tok.matches("[0-9]+"));
    }

    public int getValue()
    {
        return value;
    }
}

class BogusOp extends Operator
{

    public int priority()
    {
        return 1;
    }

    public Operand execute(Operand opd1, Operand opd2)
    {
        return null;
    }
}

class AddOp extends Operator
{

    public int priority()
    {
        return 2;
    }

    public Operand execute(Operand opd1, Operand opd2)
    {
        return new Operand(opd1.getValue() + opd2.getValue());
    }
}

class SubOp extends Operator
{

    public int priority()
    {
        return 2;
    }

    public Operand execute(Operand opd1, Operand opd2)
    {
        return new Operand(opd1.getValue() - opd2.getValue());
    }
}

class MulOp extends Operator
{

    public int priority()
    {
        return 3;
    }

    public Operand execute(Operand opd1, Operand opd2)
    {
        return new Operand(opd1.getValue() * opd2.getValue());
    }
}

class DivOp extends Operator
{

    public int priority()
    {
        return 3;
    }

    public Operand execute(Operand opd1, Operand opd2)
    {
        return new Operand(opd1.getValue() / opd2.getValue());
    }
}
