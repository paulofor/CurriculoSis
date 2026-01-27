/* tslint:disable */
import {
  PalavraChave,
  PalavraRaiz
} from '../index';

declare var Object: any;
export interface PalavraQuantidadeInterface {
  "quantidade"?: number;
  "data"?: Date;
  "palavraRaizId"?: number;
  "palavraChaveId"?: number;
  palavraChave?: PalavraChave;
  palavraRaiz?: PalavraRaiz;
}

export class PalavraQuantidade implements PalavraQuantidadeInterface {
  "quantidade": number;
  "data": Date;
  "palavraRaizId": number;
  "palavraChaveId": number;
  palavraChave: PalavraChave;
  palavraRaiz: PalavraRaiz;
  constructor(data?: PalavraQuantidadeInterface) {
    Object.assign(this, data);
  }
  /**
   * The name of the model represented by this $resource,
   * i.e. `PalavraQuantidade`.
   */
  public static getModelName() {
    return "PalavraQuantidade";
  }
  /**
  * @method factory
  * @author Jonathan Casarrubias
  * @license MIT
  * This method creates an instance of PalavraQuantidade for dynamic purposes.
  **/
  public static factory(data: PalavraQuantidadeInterface): PalavraQuantidade{
    return new PalavraQuantidade(data);
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
      name: 'PalavraQuantidade',
      plural: 'PalavraQuantidades',
      path: 'PalavraQuantidades',
      idName: 'palavraRaizId',
      properties: {
        "quantidade": {
          name: 'quantidade',
          type: 'number'
        },
        "data": {
          name: 'data',
          type: 'Date'
        },
        "palavraRaizId": {
          name: 'palavraRaizId',
          type: 'number'
        },
        "palavraChaveId": {
          name: 'palavraChaveId',
          type: 'number'
        },
      },
      relations: {
        palavraChave: {
          name: 'palavraChave',
          type: 'PalavraChave',
          model: 'PalavraChave',
          relationType: 'belongsTo',
                  keyFrom: 'palavraChaveId',
          keyTo: 'id'
        },
        palavraRaiz: {
          name: 'palavraRaiz',
          type: 'PalavraRaiz',
          model: 'PalavraRaiz',
          relationType: 'belongsTo',
                  keyFrom: 'palavraRaizId',
          keyTo: 'id'
        },
      }
    }
  }
}
