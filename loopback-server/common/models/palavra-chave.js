'use strict';

module.exports = function(Palavrachave) {

    Palavrachave.ListaPalavra = function(callback) {
        Palavrachave.find(callback);
    }
};
