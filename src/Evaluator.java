

import java.util.Stack;
import java.util.StringTokenizer;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author mandynoto
 */

public class Evaluator
{
    private Stack<Operand> opdStack;
    private Stack<Operator> oprStack;

    public Evaluator()
    {
        opdStack = new Stack<Operand>();
        oprStack = new Stack<Operator>();
    }

    public int eval(String expr)
    {
        String tok;
        String delimiters;
        String bogusOperatorSymbol = new String("#");

        oprStack.push(Operator.getOperator(bogusOperatorSymbol));
        delimiters = "+-*/# ";
        StringTokenizer st = new StringTokenizer(expr, delimiters, true);
        // the 3rd arg is true to indicate to use the delimiters as tokens, too 
        // but we'll filter out spaces
        while (st.hasMoreTokens())
        {
            if (!(tok = st.nextToken()).equals(" "))
            {  // filter out spaces
                if (Operand.check(tok))
                {   // check if tok is an operand
                    opdStack.push(new Operand(tok));
                } else
                {
                    if (!Operator.check(tok))
                    {
                        System.out.println("*****invalid token******");
                        System.exit(1);
                    }
                    Operator newOpr = Operator.getOperator(tok); // POINT 1
                    while (((Operator) oprStack.peek()).priority() >= newOpr.priority())
                    {
                        Operator oldOpr = ((Operator) oprStack.pop());
                        Operand op2 = (Operand) opdStack.pop();
                        Operand op1 = (Operand) opdStack.pop();
                        opdStack.push(oldOpr.execute(op1, op2));
                    }
                    oprStack.push(newOpr);
                }
            }
        }

        return evaluateExpression(opdStack, oprStack);
    }

    /**
     * Evaluates the operands on the specified operand stack based on the the
     * operators on the operand stack.
     */
    /**
     * Returns the result of an expression from 
     * the operands on the specified operand stack 
     * based on the operators on the operand stack.
     * 
     * @param   operandStack  the specified operand stack.
     * @param   operatorStack the specified operator stack.
     * @return  the result of an expression from the operands on the specified stack
     *          based on the operators on the operand stack.
     */
    public int evaluateExpression(Stack<Operand> operandStack, Stack<Operator> operatorStack)
    {
        Operator currentOperator;
        currentOperator = operatorStack.pop();

        while (!(currentOperator instanceof BogusOperator)) 
        {
            Operand operand2;
            Operand operand1;
            if (operandStack.empty() == true) 
            {
                break;
            }
            operand2 = (Operand) operandStack.pop();
            operand1 = (Operand) operandStack.pop();
            Operand currentExpressionResult = currentOperator.execute(operand1, operand2);
            operandStack.push(currentExpressionResult);
            currentOperator = operatorStack.pop();
        }

        return operandStack.pop().getValue();
    }
}

class BogusOperator extends Operator
{
    @Override
    int priority()
    {
        return 0;
    }

    @Override
    Operand execute(Operand opd1, Operand opd2)
    {
        return null;
    }
}

class AdditionOperator extends Operator
{
    @Override
    public int priority()
    {
        return 1;
    }

    @Override
    public Operand execute(Operand operand1, Operand operand2)
    {
        int operand1Value = operand1.getValue();
        int operand2Value = operand2.getValue();
        int operandsSumValue = operand1Value + operand2Value;
        return new Operand(operandsSumValue);
    }
}

class SubtractionOperator extends Operator
{
    @Override
    int priority()
    {
        return 1;
    }

    @Override
    Operand execute(Operand operand1, Operand operand2)
    {
        int operand1Value = operand1.getValue();
        int operand2Value = operand2.getValue();
        int operandsDifferenceValue = operand1Value - operand2Value;
        return new Operand(operandsDifferenceValue);
    }

}

class MultiplicationOperator extends Operator
{
    @Override
    int priority()
    {
        return 2;
    }

    @Override
    Operand execute(Operand operand1, Operand operand2)
    {
        int operand1Value = operand1.getValue();
        int operand2Value = operand2.getValue();
        int operandsProductValue = operand1Value * operand2Value;
        return new Operand(operandsProductValue);
    }

}

class DivisionOperator extends Operator
{
    @Override
    int priority()
    {
        return 2;
    }

    @Override
    Operand execute(Operand operand1, Operand operand2)
    {
        int operand1Value = operand1.getValue();
        int operand2Value = operand2.getValue();
        int operandsQuotientValue = operand1Value / operand2Value;
        return new Operand(operandsQuotientValue);
    }
}

abstract class Operator
{
    /**
     * Returns the priority number of this operator over others.
     *
     * That is, if this was already on the operator stack and had higher
     * priority over another, then the current operands on the operand stack
     * would be executed.
     * 
     * @return the priority number of this operator over others.
     */
    abstract int priority();

    /**
     * Returns the appropriate Operator class corresponding to the specified
     * token.
     *
     * @param token the specified token.
     * @return the appropriate Operator class corresponding to the specified
     * token.
     */

    /**
     * Returns true if the specified token is a valid operand, that is, either
     * +,-,*, or / (symbols: addition, subtraction, multiplication, division).
     *
     * @param token the specified token.
     * @return  true if the specified token  is a valid operand, that is, either
     *          +,-,*, or / (symbols: addition, subtraction, multiplication, division).
     */
    static boolean check(String token)
    {
        String delimiters = "+-*/# ";
        if (!delimiters.contains(token))
        {
            return false;
        }
        return true;
    }

    /**
     * Returns a new operand with a value based on the specified
     * operands and the type of operand.
     * For example, if the operands were 1 and 2 and the operand
     * was addition, then it would perform addition on 1 and 2, outputting
     * 1 + 2 = 3.
     * 
     * @param operand1  one of the specified operands.
     * @param operand2  one of the specified operands.
     * @return
     */
    abstract Operand execute(Operand operand1, Operand operand2);

    /**
     * Returns the corresponding operator based on the specified token.
     * 
     * @param   token the specified token.
     * @return  the corresponding operator based on the specified token.
     */
    static Operator getOperator(String token)
    {
        if (token.equals("+"))
        {
            return new AdditionOperator();
        } else if (token.equals(("-")))
        {
            return new SubtractionOperator();
        } else if (token.equals("*"))
        {
            return new MultiplicationOperator();
        } else if (token.equals("/"))
        {
            return new DivisionOperator();
        } else if (token.equals("#"))
        {
            return new BogusOperator();
        }
        return null;
    }
}

class Operand
{
    String token;
    int value;

    /**
     * Constructs an operand with the specified token, assumed to be an integer,
     * that calls another operand constructor in this same class to instantiate
     * an Integer value with the specified token.
     *
     * @param token the specified operator token.
     */
    Operand(String token)
    {
        this(Integer.parseInt(token));
        this.token = token;
    }

    /**
     * Constructs an operand with the specified value.
     *
     * @param value the specified value.
     */
    Operand(int value)
    {
        this.value = value;
    }

    /**
     * Returns true if the specified token is an integer, otherwise returns
     * false.
     *
     * @param token the specified token.
     * @return true if the specified token is an integer, otherwise returns
     * false.
     */
    static boolean check(String token)
    {
        if (token == null)
        {
            return false;
        }

        // Return false if token cannot be parsed as an integer.
        try
        {
            Integer i = Integer.parseInt(token);
        } catch (NumberFormatException nfe)
        {
            return false;
        }

        return true;
    }

    /**
     * Returns the integer value that this operand contains.
     *
     * @return the integer value that this operand contains.
     */
    int getValue()
    {
        return this.value;
    }
}
