'use strict';

module.exports = function(Oportunidadefree) {


    Oportunidadefree.RecebeItem = function(oportunidade,callback) {
        let filtro = {'where' : { 'titulo' : oportunidade.titulo }}
        Oportunidadefree.findOne(filtro,(err,result) => {
            if (result) {
                oportunidade.id = result.id;
            }
            oportunidade.data = new Date();
            //oportunidade.maisRecente = 1;
            Oportunidadefree.upsert(oportunidade,callback);
        })
    }
};
