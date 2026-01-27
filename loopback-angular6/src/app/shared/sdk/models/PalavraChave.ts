/* tslint:disable */
import {
  PalavraQuantidade,
  OportunidadePalavra
} from '../index';

declare var Object: any;
export interface PalavraChaveInterface {
  "palavra"?: string;
  "id"?: number;
  palavraQuantidades?: PalavraQuantidade[];
  oportunidadePalavras?: OportunidadePalavra[];
}

export class PalavraChave implements PalavraChaveInterface {
  "palavra": string;
  "id": number;
  palavraQuantidades: PalavraQuantidade[];
  oportunidadePalavras: OportunidadePalavra[];
  constructor(data?: PalavraChaveInterface) {
    Object.assign(this, data);
  }
  /**
   * The name of the model represented by this $resource,
   * i.e. `PalavraChave`.
   */
  public static getModelName() {
    return "PalavraChave";
  }
  /**
  * @method factory
  * @author Jonathan Casarrubias
  * @license MIT
  * This method creates an instance of PalavraChave for dynamic purposes.
  **/
  public static factory(data: PalavraChaveInterface): PalavraChave{
    return new PalavraChave(data);
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
      name: 'PalavraChave',
      plural: 'PalavraChaves',
      path: 'PalavraChaves',
      idName: 'id',
      properties: {
        "palavra": {
          name: 'palavra',
          type: 'string'
        },
        "id": {
          name: 'id',
          type: 'number'
        },
      },
      relations: {
        palavraQuantidades: {
          name: 'palavraQuantidades',
          type: 'PalavraQuantidade[]',
          model: 'PalavraQuantidade',
          relationType: 'hasMany',
                  keyFrom: 'id',
          keyTo: 'palavraChaveId'
        },
        oportunidadePalavras: {
          name: 'oportunidadePalavras',
          type: 'OportunidadePalavra[]',
          model: 'OportunidadePalavra',
          relationType: 'hasMany',
                  keyFrom: 'id',
          keyTo: 'palavraChaveId'
        },
      }
    }
  }
}
