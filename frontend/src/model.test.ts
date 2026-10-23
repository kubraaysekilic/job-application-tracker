import {describe,it,expect} from 'vitest';
import {Application,statistics,filterApplications} from './model';
const items:Application[]=[{id:1,company:'İstanbul Tech',position:'Java Developer',status:'INTERVIEW',url:'',notes:'',appliedOn:'2026-01-01',createdAt:'',version:0},{id:2,company:'Example',position:'Analyst',status:'OFFER',url:'',notes:'',appliedOn:'2026-01-01',createdAt:'',version:0}];
describe('tracking model',()=>{
 it('filters Turkish text',()=>expect(filterApplications(items,'istanbul','ALL')).toHaveLength(1));
 it('combines status and search',()=>expect(filterApplications(items,'java','OFFER')).toHaveLength(0));
 it('summarizes actual records',()=>expect(statistics(items)).toEqual({total:2,active:1,interviews:1,offers:1}));
 it('handles empty lists',()=>expect(statistics([]).total).toBe(0));
});
