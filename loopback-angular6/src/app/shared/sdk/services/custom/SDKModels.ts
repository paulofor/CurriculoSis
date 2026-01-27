/* tslint:disable */
import { Injectable } from '@angular/core';
import { User } from '../../models/User';
import { OportunidadeLinkedin } from '../../models/OportunidadeLinkedin';
import { PalavraRaiz } from '../../models/PalavraRaiz';
import { PalavraChave } from '../../models/PalavraChave';
import { PalavraQuantidade } from '../../models/PalavraQuantidade';
import { OportunidadePalavra } from '../../models/OportunidadePalavra';
import { ExperienciaProfissionalLivre } from '../../models/ExperienciaProfissionalLivre';
import { OportunidadeFree } from '../../models/OportunidadeFree';

export interface Models { [name: string]: any }

@Injectable()
export class SDKModels {

  private models: Models = {
    User: User,
    OportunidadeLinkedin: OportunidadeLinkedin,
    PalavraRaiz: PalavraRaiz,
    PalavraChave: PalavraChave,
    PalavraQuantidade: PalavraQuantidade,
    OportunidadePalavra: OportunidadePalavra,
    ExperienciaProfissionalLivre: ExperienciaProfissionalLivre,
    OportunidadeFree: OportunidadeFree,
    
  };

  public get(modelName: string): any {
    return this.models[modelName];
  }

  public getAll(): Models {
    return this.models;
  }

  public getModelNames(): string[] {
    return Object.keys(this.models);
  }
}
