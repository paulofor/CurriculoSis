/* tslint:disable */

declare var Object: any;
export interface ExperienciaProfissionalLivreInterface {
  "cliente"?: string;
  "dataInicio"?: Date;
  "dataTermino"?: Date;
  "consultoria"?: string;
  "descricaoLivre"?: string;
  "principaisTecnologias"?: string;
  "tituloFuncao"?: string;
  "descricaoGupy"?: string;
  "dataGupy"?: Date;
  "id"?: number;
}

export class ExperienciaProfissionalLivre implements ExperienciaProfissionalLivreInterface {
  "cliente": string;
  "dataInicio": Date;
  "dataTermino": Date;
  "consultoria": string;
  "descricaoLivre": string;
  "principaisTecnologias": string;
  "tituloFuncao": string;
  "descricaoGupy": string;
  "dataGupy": Date;
  "id": number;
  constructor(data?: ExperienciaProfissionalLivreInterface) {
    Object.assign(this, data);
  }
  /**
   * The name of the model represented by this $resource,
   * i.e. `ExperienciaProfissionalLivre`.
   */
  public static getModelName() {
    return "ExperienciaProfissionalLivre";
  }
  /**
  * @method factory
  * @author Jonathan Casarrubias
  * @license MIT
  * This method creates an instance of ExperienciaProfissionalLivre for dynamic purposes.
  **/
  public static factory(data: ExperienciaProfissionalLivreInterface): ExperienciaProfissionalLivre{
    return new ExperienciaProfissionalLivre(data);
  }
  /**
  * @method getModelDefinition
  * @author Julien Ledun
  * @license MIT
  * This method returns an object that represents some of the model
  * definitions.
  **/
  public static getModelDefinition() {
    return {
      name: 'ExperienciaProfissionalLivre',
      plural: 'ExperienciaProfissionalLivres',
      path: 'ExperienciaProfissionalLivres',
      idName: 'id',
      properties: {
        "cliente": {
          name: 'cliente',
          type: 'string'
        },
        "dataInicio": {
          name: 'dataInicio',
          type: 'Date'
        },
        "dataTermino": {
          name: 'dataTermino',
          type: 'Date'
        },
        "consultoria": {
          name: 'consultoria',
          type: 'string'
        },
        "descricaoLivre": {
          name: 'descricaoLivre',
          type: 'string'
        },
        "principaisTecnologias": {
          name: 'principaisTecnologias',
          type: 'string'
        },
        "tituloFuncao": {
          name: 'tituloFuncao',
          type: 'string'
        },
        "descricaoGupy": {
          name: 'descricaoGupy',
          type: 'string'
        },
        "dataGupy": {
          name: 'dataGupy',
          type: 'Date'
        },
        "id": {
          name: 'id',
          type: 'number'
        },
      },
      relations: {
      }
    }
  }
}
