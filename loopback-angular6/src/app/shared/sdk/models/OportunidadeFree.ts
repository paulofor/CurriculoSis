/* tslint:disable */

declare var Object: any;
export interface OportunidadeFreeInterface {
  "descricao"?: string;
  "titulo"?: string;
  "data"?: Date;
  "id"?: number;
}

export class OportunidadeFree implements OportunidadeFreeInterface {
  "descricao": string;
  "titulo": string;
  "data": Date;
  "id": number;
  constructor(data?: OportunidadeFreeInterface) {
    Object.assign(this, data);
  }
  /**
   * The name of the model represented by this $resource,
   * i.e. `OportunidadeFree`.
   */
  public static getModelName() {
    return "OportunidadeFree";
  }
  /**
  * @method factory
  * @author Jonathan Casarrubias
  * @license MIT
  * This method creates an instance of OportunidadeFree for dynamic purposes.
  **/
  public static factory(data: OportunidadeFreeInterface): OportunidadeFree{
    return new OportunidadeFree(data);
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
      name: 'OportunidadeFree',
      plural: 'OportunidadeFrees',
      path: 'OportunidadeFrees',
      idName: 'id',
      properties: {
        "descricao": {
          name: 'descricao',
          type: 'string'
        },
        "titulo": {
          name: 'titulo',
          type: 'string'
        },
        "data": {
          name: 'data',
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
