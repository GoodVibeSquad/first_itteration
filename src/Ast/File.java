package Ast;

import java.util.List;


public record File(List<Def> functions, Statement mainBlock) {
    //accept metode (visitor) }
}
