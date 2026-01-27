/* tslint:disable */
import {
  OportunidadeLinkedin,
  PalavraChave
} from '../index';

declare var Object: any;
export interface OportunidadePalavraInterface {
  "palavraChaveId"?: number;
  "oportunidadeLinkedinId"?: number;
  oportunidadeLinkedin?: OportunidadeLinkedin;
  palavraChave?: PalavraChave;
}

export class OportunidadePalavra implements OportunidadePalavraInterface {
  "palavraChaveId": number;
  "oportunidadeLinkedinId": number;
  oportunidadeLinkedin: OportunidadeLinkedin;
  palavraChave: PalavraChave;
  constructor(data?: OportunidadePalavraInterface) {
    Object.assign(this, data);
  }
  /**
   * The name of the model represented by this $resource,
   * i.e. `OportunidadePalavra`.
   */
  public static getModelName() {
    return "OportunidadePalavra";
  }
  /**
  * @method factory
  * @author Jonathan Casarrubias
  * @license MIT
  * This method creates an instance of OportunidadePalavra for dynamic purposes.
  **/
  public static factory(data: OportunidadePalavraInterface): OportunidadePalavra{
    return new OportunidadePalavra(data);
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
      name: 'OportunidadePalavra',
      plural: 'OportunidadePalavras',
      path: 'OportunidadePalavras',
      idName: 'palavraChaveId',
      properties: {
        "palavraChaveId": {
          name: 'palavraChaveId',
          type: 'number'
        },
        "oportunidadeLinkedinId": {
          name: 'oportunidadeLinkedinId',
          type: 'number'
        },
      },
      relations: {
        oportunidadeLinkedin: {
          name: 'oportunidadeLinkedin',
          type: 'OportunidadeLinkedin',
          model: 'OportunidadeLinkedin',
          relationType: 'belongsTo',
                  keyFrom: 'oportunidadeLinkedinId',
          keyTo: 'id'
        },
        palavraChave: {
          name: 'palavraChave',
          type: 'PalavraChave',
          model: 'PalavraChave',
          relationType: 'belongsTo',
                  keyFrom: 'palavraChaveId',
          keyTo: 'id'
        },
      }
    }
  }
}
