/*

Service to share data among different components

*/

import { Injectable } from '@angular/core';

@Injectable()
export class SharingService{
    private data:any = undefined;

    setData(data:any){
        this.data = data;
    }

    getData():any{
        return this.data;
    }
}