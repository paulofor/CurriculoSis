'use strict';

module.exports = function(Palavraquantidade) {

    Palavraquantidade.TotalizaPalavraChave = function(palavraRaiz, palavraChave, callback) {
        const ds = Palavraquantidade.dataSource;
    }

    Palavraquantidade.InicializaPorRaiz = function(idPalavraRaiz,callback) {
        const sql = "delete from PalavraQuantidade where palavraRaizId = " + idPalavraRaiz;
        const ds = Palavraquantidade.dataSource;
        ds.connect.query(sql,callback);
    }
};
