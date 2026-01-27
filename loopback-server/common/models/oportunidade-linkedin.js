'use strict';

module.exports = function(Oportunidadelinkedin) {

    Oportunidadelinkedin.RecebeItem = function(oportunidade,callback) {
        let filtro = {'where' : {'and' : [{'url' : oportunidade.url} , {'palavraRaizId' : oportunidade.palavraRaizId}]}}
        Oportunidadelinkedin.findOne(filtro,(err,result) => {
            if (result) {
                oportunidade.id = result.id;
            }
            oportunidade.data = new Date();
            oportunidade.maisRecente = 1;
            Oportunidadelinkedin.upsert(oportunidade,callback);
        })
    }

    Oportunidadelinkedin.RegistraEnvio = function(idOportunidade, callback) {
        const sql = "update OportunidadeLinkedin set dataEnvio = now() where id = " + idOportunidade;
        Oportunidadelinkedin.dataSource.connector.query(sql,callback);
    }

    Oportunidadelinkedin.ObtemPorChaveRaiz = function(idPalvraRaiz, idPalavraChave, callback) {
        const sql = "select id, convert(descricao using utf8) as descricao, tempo, modelo, url, dataEnvio, maisRecente, " +
            " data, convert(empresa using utf8) as empresa, " +
            " url, volume, convert(titulo using utf8) as titulo from OportunidadeLinkedin  " +
            " inner join OportunidadePalavra on OportunidadePalavra.oportunidadeLinkedinId = OportunidadeLinkedin.id " +
            " where palavraRaizId = " + idPalvraRaiz +
            " and OportunidadePalavra.palavraChaveId = " + idPalavraChave +
            " order by data desc";
        Oportunidadelinkedin.dataSource.connector.query(sql,callback);
    }
};
