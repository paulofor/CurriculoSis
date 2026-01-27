'use strict';

module.exports = function(Palavraraiz) {

    Palavraraiz.ListaParaVisita = function(callback) {
        let sqlLimpaOportunidade = "update OportunidadeLinkedin set maisRecente = 0";
        const ds = Palavraraiz.dataSource;
        ds.connector.query(sqlLimpaOportunidade, (err,result) => {
            let filtro = {'where' : {'ativo' : 1}}
            Palavraraiz.find(filtro,callback);
        })
    }    

    Palavraraiz.AtualizaQuantidadeGeral = function(callback) {
        const ds = Palavraraiz.dataSource;
        const sql1 = "update OportunidadeLinkedin set descricaoTexto = descricao";
        const sql2 = "delete from OportunidadePalavra";
        const sql3 = "INSERT INTO OportunidadePalavra (palavraChaveId, oportunidadeLinkedinId) " +
                " SELECT " +
                " pc.id AS palavraChaveId, " +
                " ol.id AS oportunidadeLinkedinId " +
                " FROM PalavraChave pc " +
                " JOIN OportunidadeLinkedin ol ON INSTR(LOWER(ol.descricaoTexto), LOWER(pc.palavra)) > 0 " +
                " WHERE	ol.data >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) ";
        const sql4 = "delete from PalavraQuantidade";
        const sql5 = "INSERT INTO PalavraQuantidade (palavraChaveId, palavraRaizId, quantidade, data) " +
                "SELECT " +
                " op.palavraChaveId, ol.palavraRaizId, COUNT(*) AS quantidade, NOW() AS data " +
                " FROM OportunidadePalavra op " +
                " JOIN OportunidadeLinkedin ol ON op.oportunidadeLinkedinId = ol.id " +
                " GROUP BY op.palavraChaveId, ol.palavraRaizId";
        const sql6 = "update PalavraRaiz set quantidade = " + 
            " (select count(*) from OportunidadeLinkedin " +
            " where data >= DATE_SUB(CURDATE(), INTERVAL 3 MONTH) and descricao is not null and PalavraRaiz.id = OportunidadeLinkedin.palavraRaizId " +
            " )";
        ds.connector.query(sql1, (err,result1) => {
            //console.log('err1', err);
            ds.connector.query(sql2, (err,result2) => {
                //console.log('err2', err);
                ds.connector.query(sql3, (err,result3) => {
                    //console.log('err3', err);
                    ds.connector.query(sql4, (err,result4) => {
                        //console.log('err4', err);
                        ds.connector.query(sql5, (err,result5) => {
                            //console.log('err5', err);
                            ds.connector.query(sql6,callback);
                        })
                    })
                })
            })
        })
    }
};
