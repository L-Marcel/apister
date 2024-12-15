package app.core;

// [TODO] Complete esse enum;
// [TODO] Crie o construtor;
// [TODO] Crie um método estático que recebe um valor
// inteiro e retorna o seu respectivo enum;
// [TODO] Crie um método que retorna o valor 
// inteiro do enum.

public enum StatusCode {
    OK(200);
    
    private final int code;

    StatusCode(int code) {
        this.code = code;
    }

    // Criei esses metodos só para testar o request.
    public static StatusCode fromCode(int code) {
        for (StatusCode status : values()) {
            if (status.code == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Código de status não suportado: " + code);
    }
};
