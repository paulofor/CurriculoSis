/* tslint:disable */
import {
  OportunidadeLinkedin,
  PalavraQuantidade
} from '../index';

declare var Object: any;
export interface PalavraRaizInterface {
  "palavra"?: string;
  "ativo"?: number;
  "quantidade"?: number;
  "id"?: number;
  oportunidadeLinkedins?: OportunidadeLinkedin[];
  palavraQuantidades?: PalavraQuantidade[];
}

export class PalavraRaiz implements PalavraRaizInterface {
  "palavra": string;
  "ativo": number;
  "quantidade": number;
  "id": number;
  oportunidadeLinkedins: OportunidadeLinkedin[];
  palavraQuantidades: PalavraQuantidade[];
  constructor(data?: PalavraRaizInterface) {
    Object.assign(this, data);
  }
  /**
   * The name of the model represented by this $resource,
   * i.e. `PalavraRaiz`.
   */
  public static getModelName() {
    return "PalavraRaiz";
  }
  /**
  * @method factory
  * @author Jonathan Casarrubias
  * @license MIT
  * This method creates an instance of PalavraRaiz for dynamic purposes.
  **/
  public static factory(data: PalavraRaizInterface): PalavraRaiz{
    return new PalavraRaiz(data);
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
      name: 'PalavraRaiz',
      plural: 'PalavraRaizs',
      path: 'PalavraRaizs',
      idName: 'id',
      properties: {
        "palavra": {
          name: 'palavra',
          type: 'string'
        },
        "ativo": {
          name: 'ativo',
          type: 'number'
        },
        "quantidade": {
          name: 'quantidade',
          type: 'number'
        },
        "id": {
          name: 'id',
          type: 'number'
        },
      },
      relations: {
        oportunidadeLinkedins: {
          name: 'oportunidadeLinkedins',
          type: 'OportunidadeLinkedin[]',
          model: 'OportunidadeLinkedin',
          relationType: 'hasMany',
                  keyFrom: 'id',
          keyTo: 'palavraRaizId'
        },
        palavraQuantidades: {
          name: 'palavraQuantidades',
          type: 'PalavraQuantidade[]',
          model: 'PalavraQuantidade',
          relationType: 'hasMany',
                  keyFrom: 'id',
          keyTo: 'palavraRaizId'
        },
      }
    }
  }
}
